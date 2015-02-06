import se.qbranch.tickets.Ticket;

import javax.xml.bind.JAXB;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;

public class TicketSubscriber {

    public static void main(final String[] args) {
        final ServiceBusContract service = createContract();
        try {
            final ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
            opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
            while(true) {
                final ReceiveSubscriptionMessageResult resultSubMsg = service.receiveSubscriptionMessage("topic",
                        "subscription", opts);
                final BrokeredMessage message = resultSubMsg.getValue();
                if((message != null) && (message.getMessageId() != null)) {
                    final Ticket t = JAXB.unmarshal(message.getBody(), Ticket.class);
                    System.out.println(t.getTicketId());
                    System.out.println(t.getCustomerId());
                    System.out.println(t.getTicketDescription());
                    System.out.println("-----------");
                }
                else {
                    System.out.println("No new messages");
                }
            }
        }
        catch(final Exception e) {
            e.printStackTrace();
        }
    }

    private static ServiceBusContract createContract() {
        final String namespace = "namespace";
        final String user = "user";
        final String password = "password";
        final Configuration config =
                ServiceBusConfiguration.configureWithWrapAuthentication(
                        namespace,
                        user,
                        password,
                        ".servicebus.windows.net",
                        "-sb.accesscontrol.windows.net/WRAPv0.9");
        final ServiceBusContract service = ServiceBusService.create(config);
        return service;
    }
}
