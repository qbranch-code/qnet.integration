import static java.lang.System.getProperty;
import static java.lang.System.getenv;

import se.qbranch.tickets.Ticket;

import java.util.Optional;

import javax.xml.bind.JAXB;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
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
            while(true) {
                receive(service).ifPresent(TicketSubscriber::debug);
            }
        }
        catch(final Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<Ticket> receive(final ServiceBusContract service) throws ServiceException {
        final ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
        opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
        final String topic = config("topic");
        final String subscription = config("subscription");
        final ReceiveSubscriptionMessageResult result = service.receiveSubscriptionMessage(topic, subscription, opts);
        final BrokeredMessage message = result.getValue();
        if((message != null) && (message.getMessageId() != null)) {
            return Optional.of(JAXB.unmarshal(message.getBody(), Ticket.class));
        }
        return Optional.empty();
    }

    private static void debug(final Ticket t) {
        System.out.println(t.getTicketId());
        System.out.println(t.getCustomerId());
        System.out.println(t.getTicketDescription());
        System.out.println("-----------");
    }

    private static ServiceBusContract createContract() {
        final Configuration config = ServiceBusConfiguration.configureWithWrapAuthentication(
                config("namespace"),
                config("user"),
                config("password"),
                ".servicebus.windows.net",
                "-sb.accesscontrol.windows.net/WRAPv0.9");
        return ServiceBusService.create(config);
    }

    private static String config(final String name) {
        return Optional.ofNullable(getenv(name)).orElseGet(() -> getProperty(name));
    }
}
