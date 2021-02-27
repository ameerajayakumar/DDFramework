package com.luna.rough;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.luna.utilities.MonitoringMail;
import com.luna.utilities.TestConfig;

public class HostAddress {
	public static void main(String[] args) throws UnknownHostException, AddressException, MessagingException {
		
		MonitoringMail mail = new MonitoringMail();
		String messageBody = "http://"+InetAddress.getLocalHost().getHostAddress()+":8383/job/DataDrivenLiveProject/Extent_20Reports/";
		System.out.println(messageBody);
		mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messageBody);
	}

}
