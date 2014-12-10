using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using Microsoft.ServiceBus;
using Microsoft.ServiceBus.Messaging;
using Microsoft.WindowsAzure;
using schemas.qbranch.se._2014._04.ticket;

namespace TicketSubscriber
{
    class Program
    {
        static void Main(string[] args)
        {
            var connectionString = CloudConfigurationManager.GetSetting("Microsoft.ServiceBus.ConnectionString");
            var topic = CloudConfigurationManager.GetSetting("topic");
            var subscription = CloudConfigurationManager.GetSetting("subscription");

            var client = SubscriptionClient.CreateFromConnectionString(connectionString, topic, subscription);

            while (true)
            {
                BrokeredMessage message = client.Receive();

                if (message != null)
                {
                    try
                    {
                        if (message.Properties.Contains(new KeyValuePair<string, object>("messagetype", "ticket")))
                        {
                            var ticket = message.GetBody<ticket>();
                            Console.WriteLine("ticket_number: " + ticket.ticket_id);
                        }
                        
                        message.Complete();

                    }
                    catch (Exception ex)
                    {
                        // Indicate a problem, unlock message in subscription
                        Console.WriteLine(ex);
                        message.Abandon();
                    }
                }
            } 
        }
    }
}
