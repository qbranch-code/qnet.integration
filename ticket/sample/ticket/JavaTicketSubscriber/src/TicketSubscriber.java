import javax.xml.bind.JAXB;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*; 
import se.qbranch.tickets.Ticket;

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
		        ReceiveSubscriptionMessageResult  resultSubMsg = service.receiveSubscriptionMessage("message-out-from-qnet-dev", "8001-ticket", opts);
		        BrokeredMessage message = resultSubMsg.getValue();
		        
		        if (message != null && message.getMessageId() != null)
		        {	
		        	Ticket t = JAXB.unmarshal(message.getBody(), Ticket.class);
		        	
		        	System.out.println(t.getTicketId());
		        	System.out.println(t.getCustomerId());
		        	System.out.println(t.getTicketDescription());
		        	System.out.println("-----------");
		        }
		        else
		        {
		        	System.out.println("No new messages");
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

