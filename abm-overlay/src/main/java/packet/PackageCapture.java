package packet;

public class PackageCapture 
{
	
	/*
	public static void main(String[] args) {
		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
														// NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r != Pcap.OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return;
		}
		System.out.println("Network devices found:");
		int i = 0;
		for (PcapIf device : alldevs) {
			String description = (device.getDescription() != null) ? device.getDescription()
					: "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
		}
		PcapIf device = alldevs.get(0); // Get first device in list
		System.out.printf("\nChoosing '%s' on your behalf:\n",
				(device.getDescription() != null) ? device.getDescription() : device.getName());
		int snaplen = 64 * 1024; // Capture all packets, no trucation
		int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
		int timeout = 10 * 1000; // 10 seconds in millis
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return;
		}

		final Crypt c = new Crypt();
		c.enableKey();
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
			public void nextPacket(PcapPacket packet, String user) {
				byte[] data = packet.getByteArray(0, packet.size()); // the
																		// package
																		// data
				byte[] sIP = new byte[4];
				byte[] dIP = new byte[4];
				Ip4 ip = new Ip4();
				Ip6 ipv6 = new Ip6();
				Tcp tcp = new Tcp(); // Preallocate a Tcp header

				if (packet.hasHeader(ip) == false) {
					return; // Not IP packet
				}
				ip.source(sIP);
				ip.destination(dIP);
				/* Use jNetPcap format utilities 
				String sourceIP = null;
				String descIP = null;
				try {
					sourceIP = InetAddress.getByAddress(sIP).getHostAddress();
					descIP = InetAddress.getByAddress(dIP).getHostAddress();

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (packet.hasHeader(tcp)) 
				{
					System.out.println(tcp.destination() + " " + tcp.source());

					Ethernet eth = new Ethernet();
					if (packet.hasHeader(eth)) 
					{
						
						byte[] O = eth.destination();
						
						System.out.println(Arrays.toString(O));
						System.out.println(O);
						System.out.println(new String(O));
						System.out.println(bytesToHex(O));
						
						
						System.out.println("before");
						for(byte b : eth.destination())
						{
							System.out.print(b + " " );
						}
						System.out.println("\nafter");

					}
					/*
					 * //58870 is aion chat server. if((tcp.destination() ==
					 * 58870 || tcp.destination() == 10241)) {
					 * 
					 * XmlFormatter out = new XmlFormatter(System.out); try {
					 * out.format(packet); System.out.println(out.toString()); }
					 * catch (IOException e) { //  e.printStackTrace(); }
					 * 
					 * System.out.println(tcp); System.out.println(
					 * "found packet with tcp port=" + tcp.destination());
					 * 
					 * ByteBuffer b = ByteBuffer.wrap(data); boolean result =
					 * false;// c.decrypt(b); System.out.println( result +
					 * " srcIP=" + sourceIP + " dstIP=" + descIP + " caplen=\n"
					 * + packet.toHexdump()); // " caplen=" +
					 * packet.getCaptureHeader().caplen());;
					 * 
					 * Payload payload = new Payload();
					 * if(packet.hasHeader(payload)) { byte[] payloadContent =
					 * payload.getByteArray(0, payload.size()); // offset,
					 * length
					 * 
					 * SortedMap<String, Charset> map =
					 * Charset.availableCharsets(); for(Charset c :
					 * map.values()) { System.out.println("charset = ["+c +
					 * "] \t\t\t" + new String(payloadContent, c)); } } }
					 
				}
			}

	};
	// capture first 10 packages
	pcap.loop(1000,jpacketHandler,"jNetPcap");pcap.close();}

	public static String toHexString(byte[] bytes) {
		char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v / 16];
			hexChars[j * 2 + 1] = hexArray[v % 16];
		}
		return new String(hexChars);
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String bb_to_str(ByteBuffer buffer) {
		byte[] bytes;
		if (buffer.hasArray()) {
			bytes = buffer.array();
		} else {
			bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
		}
		return new String(bytes, Charset.defaultCharset());
	}
	*/
}