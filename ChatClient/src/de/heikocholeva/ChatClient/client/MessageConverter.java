package de.heikocholeva.ChatClient.client;

public class MessageConverter {
	
	public static String toMessage(String input) {
		String output = input	.replaceAll("%PRTCL_1%", "{")
								.replaceAll("%PRTCL_2%", "}")
								.replaceAll("%PRTCL_3%", "[")
								.replaceAll("%PRTCL_4%", "]")
								.replaceAll("%PRTCL_5%", "\"")
								.replaceAll("%PRTCL_6%", ",")
								.replaceAll("%PRTCL_7%", ":")
								;
		return output;
	}
	
	public static String toConverted(String input) {
		String output = input	
								.replaceAll("%PRTCL_PRIVATE%", "")
								.replaceAll("\\{", "%PRTCL_1%")
								.replaceAll("\\}", "%PRTCL_2%")
								.replaceAll("\\[", "%PRTCL_3%")
								.replaceAll("\\]", "%PRTCL_4%")
								.replaceAll("\"", "%PRTCL_5%")
								.replaceAll("\\,", "%PRTCL_6%")
								.replaceAll("\\:", "%PRTCL_7%")
								;
		return output;
	}

}
