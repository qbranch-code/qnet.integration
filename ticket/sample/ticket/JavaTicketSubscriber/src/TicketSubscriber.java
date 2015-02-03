import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*; 


public class TicketSubscriber {
	public static void main(String[] args) {
		
		Configuration config = 
			    ServiceBusConfiguration.configureWithWrapAuthentication(
			      "namespace",
			      "user",
			      "password",
			      ".servicebus.windows.net",
			      "-sb.accesscontrol.windows.net/WRAPv0.9");

			ServiceBusContract service = ServiceBusService.create(config);


			try
			{
			    ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			    opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

			    while(true)  { 
			        ReceiveSubscriptionMessageResult  resultSubMsg = service.receiveSubscriptionMessage("message-out-from-qnet", "2822-ticket", opts);
			        BrokeredMessage message = resultSubMsg.getValue();
			        if (message != null && message.getMessageId() != null)
			        {
			        	System.out.println(message.getMessageId());
			        	System.out.println(message.getProperty("customerid"));
			        	System.out.println(message.getProperty("messagetype"));
			        	
//			        	byte[] b = new byte[200];
//			            String s = null;
//			            int numRead = message.getBody().read(b);
//			            while (-1 != numRead)
//			            {
//			                s = new String(b);
//			                s = s.trim();
//			                System.out.print(s);
//			                numRead = message.getBody().read(b);
//			            }
//			            System.out.println();
			        	
			        }
			        }
			}
			catch(Exception e)
			{
				System.out.print("ServiceException encountered: ");
			    System.out.println(e.getMessage());
			    System.exit(-1);
			}
	}
}

