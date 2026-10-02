package org.example;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketSniffer {

    Scanner scanner = new Scanner(System.in);
    LinkList history = new LinkList();


    String setFilter = "";




    //ipv4 attributes
    String srcIP;
    String desIP;
    String protocol = "";
    private double time;
    private int length;
    private String info;
    private byte[] rawData;
    StringBuilder description;
    private  boolean isCapturing;
    String desMac;
    String srcMac;


    private static BlockingQueue<byte[]> packetQueue = new LinkedBlockingQueue<>();
    public static List<PcapNetworkInterface> devs;

    private PacketListener packetListener;
    PcapHandle handle;
    public PacketSniffer(){
        isCapturing = true;
    }


    private NetworkGraph networkGraph = new NetworkGraph();

    public void setPacketListener(PacketListener listener){
        this.packetListener = listener;
    }

    public String getSetFilter() {
        return setFilter;
    }


    public NetworkGraph getNetworkGraph() {
        return networkGraph;
    }


    public static List<PcapNetworkInterface> getInterfaces(){
        try{
            devs = Pcaps.findAllDevs();
        }
        catch(Exception e){
            System.out.println(e);
        }

        return devs;
    }



    public void capturePackets(int networkInterface) throws Exception{

            if(handle != null && handle.isOpen()){
                return;
            }

            isCapturing = true;

            PcapNetworkInterface nif = devs.get(networkInterface);
            handle = nif.openLive(65536,PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,10);

            //Setting filters


            // --------Capturing thread---------

            Thread captureThread = new Thread(() -> {

                try{
                    while (isCapturing){

                        try{
                            String filter = setFilter;
                            handle.setFilter(filter,BpfProgram.BpfCompileMode.OPTIMIZE);


                        }catch (Exception e){
                            e.printStackTrace();
                        }



                        Packet packet = handle.getNextPacket();
                        if(packet != null){
                            packetQueue.offer(packet.getRawData());
                        }
                    }

                }
                catch (Exception e){
                    System.out.println(e);
                }


            });

            captureThread.start();


            //------------Processing Thread--------

            Thread processingThread = new Thread(() ->{
                while (isCapturing){
                    int packetCounter = 0;
                    try {
                        byte[] data = packetQueue.take();
                        parsingPacket(data);
                        this.rawData = data;
                        CapturedPacket newPacket = new CapturedPacket(packetCounter++, System.currentTimeMillis(),srcIP,desIP,protocol,data.length, this.info, data, new StringBuilder(this.description));
                        history.insertAtEnd(newPacket);
                        networkGraph.addInteraction(this.srcIP, this.desIP, srcMac, desMac, this.protocol);
                    }
                    catch (Exception e){
                       e.printStackTrace();
                    }


                }


            });

            processingThread.start();

    }

    public void stopCapturing(){
        if(isCapturing){
            isCapturing = false;
            if(handle != null && handle.isOpen()){
                handle.close();
            }
        }

    }


    public void parsingPacket(byte[] data){

         description = new StringBuilder();

        //-----Decoding Ethernet header----

        String desMac = String.format("%02X:%02X:%02X:%02X:%02X:%02X",data[0],data[1],data[2],data[3],data[4],data[5]);
        String sorMac = String.format("%02X:%02X:%02X:%02X:%02X:%02X",data[6],data[7],data[8],data[9],data[10],data[11]);

        this.desMac = desMac;
        this.srcMac = sorMac;

        System.out.println("Source MAC: " + sorMac);
        System.out.println("Destination MAC: " +desMac);

        int etherType = (data[12] & 0xFF) << 8 | (data[13] & 0xFF);

        description.append("--------Ethernet Header--------").append(System.lineSeparator());
        description.append("Source MAC: ").append(sorMac).append("Destination MAC: ").append(desMac).append(System.lineSeparator());
        description.append("Ethernet Type: ").append(etherType).append(System.lineSeparator());


        if(etherType == 0x0800){
            System.out.println("IPv4 is detected");
            description.append("IPv4 is detected").append(System.lineSeparator());
            ipv4Decode(data);
        }
        else if(etherType == 0x86DD){
            System.out.println("IPv6 is detected");
            description.append("IPv6 is detected").append(System.lineSeparator());
            ipv6Decode(data);
        }
        else if(etherType == 0x0806){
            System.out.println("ARP is detected");
            description.append("ARP is detected").append(System.lineSeparator());
            arpDecoder(data);

        }




    }

    public void ipv4Decode(byte[] data){
        int ipStart = data[14];
        int ipStartIndex = 14;

        description.append("------IPv4------").append(System.lineSeparator());

        int version = (ipStart >> 4) &0xF ;
        int ihl = (ipStart & 0xF) * 4;
        description.append("Version").append(version).append(System.lineSeparator());
        description.append("Internet Header Length(ihl): ").append(ihl).append(System.lineSeparator());

        System.out.println("Version: "+ version);
        System.out.println("Internet Header Length: "+ ihl);

        // source and destination IP
        srcIP =(data[ipStartIndex+12] & 0xFF)+"."+(data[ipStartIndex+13]&0xFF)+"."+(data[ipStartIndex+14]&0xFF)+"."+(data[ipStartIndex+15]&0xFF);
        desIP = (data[ipStartIndex+ 16] & 0xFF)+"."+(data[ipStartIndex+17]&0xFF)+"."+(data[ipStartIndex+18]&0xFF)+"."+(data[ipStartIndex+19]&0xFF);

        System.out.println("Source IP: "+ srcIP);
        System.out.println("Destination IP: "+ desIP);

        description.append("Source IP: ").append(srcIP).append(System.lineSeparator());
        description.append("Destination IP: ").append(desIP).append(System.lineSeparator());


        int protocol = data[ipStartIndex + 9] & 0xFF;
        description.append("Protocol: ").append(srcIP);
        if(protocol == 1){
            System.out.println("Protocol is ICMP");
            this.protocol = "ICMP";
            description.append("Protocol is ICMP ").append(System.lineSeparator());
            icmpDecoder(data,ipStartIndex,ihl);
        }
        if(protocol == 6){
            System.out.println("Protocol is TCP");
            description.append("Protocol is TCP ").append(System.lineSeparator());
            this.protocol = "TCP";
            tcpDecoder(data,ipStartIndex,ihl);
        }
        if(protocol == 17){
            System.out.println("Protocol is UDP");
            description.append("Protocol is UDP ").append(System.lineSeparator());
            this.protocol = "UDP";
            udpDecoder(data,ipStartIndex,ihl);
        }

        if(packetListener != null){
            packetListener.packetInfo(srcIP,desIP,this.protocol,length,info,rawData,description);
        }



    }

    public void ipv6Decode(byte[] rawData){

        int ipStart = rawData[14];
        int ipStartIndex = 14;

        description.append("------IPv6 Header------").append(System.lineSeparator());

        int version = (ipStart >> 4) &0xF;
        int priority = ((rawData[ipStartIndex] & 0xF) << 4) | ((rawData[ipStartIndex + 1] >> 4) & 0xF);

        description.append("Version: ").append(version).append(System.lineSeparator());
        description.append("Priority: ").append(priority).append(version).append(System.lineSeparator());

        //---calculating sourceIP---
        int srcStart = ipStartIndex + 8;
        String tempSrcIp = ipv6Address(rawData, srcStart);

        this.srcIP = tempSrcIp;


        //---calculating destinationIP---

        int desStart = ipStartIndex + 24;
        String tempDesIp = ipv6Address(rawData,desStart);

        this.desIP = tempDesIp;

        description.append("Source IP: ").append(tempSrcIp).append(System.lineSeparator());
        description.append("Destination IP: ").append(tempDesIp).append(System.lineSeparator());


        if(packetListener != null){
            packetListener.packetInfo(srcIP,desIP,this.protocol,length,info,rawData,description);
        }


    }

    private String ipv6Address(byte[] rawData, int start){

        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < 16; i+=2){
            int address = (rawData[start+i] &0xFF)<<8 | (rawData[start+i+1] &0xFF);
            temp.append(Integer.toHexString(address));
            if(i < 14)
                temp.append(":");

        }
        return temp.toString();

    }


    public void arpDecoder(byte[] rawData){
        int arpStart = rawData[14];
        int arpStartIndex = 14;

        description.append("------ARP Protocol-----").append(System.lineSeparator());

        int hardwareType = ((arpStart &0xFF)<<8) | ((arpStart +1 &0xFF));
        int protocolType = ((arpStart +2 &0xFF)<<8) | ((arpStart +3 &0xFF));

        int hardwareSize = ((arpStart +4) &0xFF);
        int protocolSize = ((arpStart +5) &0xFF);

        int opcode = ((arpStart +6)&0xFF)<<8 | ((arpStart +7)&0xFF);

        description.append("Hardware Type").append(hardwareType).append(System.lineSeparator());
        description.append("Protocol Type").append(protocolType).append(System.lineSeparator());
        description.append("Hardware Size").append(hardwareSize).append(System.lineSeparator());
        description.append("Protocol Size").append(protocolSize).append(System.lineSeparator());
        description.append("Opcode").append(opcode).append(System.lineSeparator());


        String arpSenderMac = String.format("%02X:%02X:%02X:%02X:%02X:%02X",rawData[8],rawData[9],rawData[10],rawData[11],rawData[12],rawData[13]);
        String arpSenderIp = ((rawData[arpStartIndex+14] &0xFF))+"."+((rawData[arpStartIndex+15] &0xFF))+"."+((rawData[arpStartIndex+16] &0xFF))+"."+((rawData[arpStartIndex+17] &0xFF));

        String arpTargetMac = String.format("%02X:%02X:%02X:%02X:%02X:%02X",rawData[17],rawData[18],rawData[19],rawData[20],rawData[21],rawData[22]);
        String arpTargetIp = ((rawData[arpStartIndex+23] &0xFF))+"."+((rawData[arpStartIndex+24] &0xFF))+"."+((rawData[arpStartIndex+25] &0xFF))+"."+((rawData[arpStartIndex+26] &0xFF));

        description.append("Sender MAC").append(arpSenderMac).append(System.lineSeparator());
        description.append("Target MAC").append(arpTargetMac).append(System.lineSeparator());
        description.append("Sender IP").append(arpSenderIp).append(System.lineSeparator());
        description.append("Target IP").append(arpTargetIp).append(System.lineSeparator());


        if(packetListener != null){
            packetListener.packetInfo(srcIP,desIP,this.protocol,length,info,rawData,description);
        }


    }







    public void icmpDecoder(byte[] data, int ipStart, int ihl){

        int icmpStart = ipStart + ihl;

        description.append("-----ICMP----").append(System.lineSeparator());

        int type = data[icmpStart] &0xFF;
        int code = data[icmpStart+1] & 0xFF;
        String typeName = "";

        description.append("Type: ").append(type).append(System.lineSeparator());
        description.append("Code: ").append(code).append(System.lineSeparator());

        if(type == 0 || type == 8){
            int ID = (data[icmpStart+4] << 8) &0xFF | (data[icmpStart+5] << 8) &0xFF;
            int Sequence = (data[icmpStart+6] << 8) &0xFF | (data[icmpStart+7] << 8) &0xFF;

            description.append("ID ").append(ID).append(System.lineSeparator());
            description.append("Sequence: ").append(Sequence).append(System.lineSeparator());

            if(type == 0){
                System.out.println("ICMP echo reply");
                description.append("ICMP echo reply").append(System.lineSeparator());
                typeName = "ICMP echo reply";
            }
            else {
                System.out.println("ICMP echo request");
                description.append("ICMP echo reply").append(System.lineSeparator());
                typeName = "ICMP echo request";
            }

            System.out.println("ICMP ID: "+ ID+ ", Sequence no. "+ Sequence);
        }

        this.info = "Type: "+typeName;

    }

    public void tcpDecoder(byte[] data, int ipStart, int ihl){

        description.append("-----TCP Header-----").append(System.lineSeparator());

        int tcpStart = ipStart+ihl;

        int srcPort = (data[tcpStart] & 0xFF) <<8 | (data[tcpStart+1] &0xFF);
        int desPort = (data[tcpStart+2] & 0xFF) <<8  | (data[tcpStart+3] &0xFF);

        description.append("Source Port").append(srcPort).append(System.lineSeparator());
        description.append("Destination Port").append(desPort).append(System.lineSeparator());

        System.out.println("Source Port: "+ srcPort);
        System.out.println("Destination Port: "+ desPort);

        long sequence = 0;

        for(int i = 0; i < 4; i++){
            sequence = ((sequence << 8) | data[tcpStart+4+i]  &0xFF);
        }
        System.out.println("Sequence no. "+sequence);
        description.append("Sequence no. ").append(sequence).append(System.lineSeparator());

        long ack = 0;
        for(int i = 0; i < 4; i++){
            ack = (ack << 8 | data[tcpStart+4+i] & 0xFF);
        }
        System.out.println("ACK: "+ack);
        description.append("Ack: ").append(ack).append(System.lineSeparator());

        int flagByte = data[tcpStart+13] &0xFF;

        boolean isUrg = (flagByte & 0x20) !=0;
        boolean isAck = (flagByte & 0x10) !=0;
        boolean isPsh = (flagByte & 0x08) !=0;
        boolean isRst = (flagByte & 0x04) !=0;
        boolean isSyn = (flagByte & 0x02) !=0;
        boolean isFin = (flagByte & 0x01) !=0;

        StringBuilder flagString = new StringBuilder();
        flagString.append("[");
        if(isUrg)flagString.append("URG");
        if(isAck)flagString.append("ACK");
        if(isPsh)flagString.append("PSH");
        if(isRst)flagString.append("RST");
        if(isFin)flagString.append("FIN");
        if(isSyn)flagString.append("SYN");
        flagString.append("]");


        this.info = srcPort+ "->"+desPort+" "+flagString.toString()+" Seq="+sequence+" Ack="+ack;
        description.append("Flag: ").append(flagString).append(System.lineSeparator());




    }

    public void udpDecoder(byte[] data , int ipStart, int ihl){

        description.append("------UDP Header------").append(System.lineSeparator());

        int udpStart = ipStart+ihl;

        int srcPort = (data[udpStart] << 8) & 0xFF | (data[udpStart+1])&0xFF;
        int desPort = (data[udpStart+2] << 8) & 0xFF | (data[udpStart+3])&0xFF;

        System.out.println("Source Port: "+ srcPort);
        System.out.println("Destination Port: "+ desPort);

        this.info = "Source Port: "+srcPort+" Destination Port: "+desPort;
        description.append("Source Port: ").append(srcPort).append(System.lineSeparator());
        description.append("Destination Port: ").append(desPort).append(System.lineSeparator());


    }


    public String getSrcIP() {
        return srcIP;
    }

    public String getDesIP() {
        return desIP;
    }



}