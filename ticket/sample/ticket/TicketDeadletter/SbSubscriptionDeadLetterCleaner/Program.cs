using System;
using System.IO;
using System.Runtime.Serialization;
using Microsoft.ServiceBus.Messaging;
using Newtonsoft.Json;
using schemas.qbranch.se._2014._04.ticket;

namespace SbSubscriptionDeadLetterCleaner
{
    class Program
    {
        static void Main(string[] args)
        {
            var customer_id = "[customer_id]";
            var password = "[password]";
            var user = "[user]";
            var connectionString = string.Format("Endpoint=sb://qbranch-qnet-ew.servicebus.windows.net/;SharedSecretIssuer={0};SharedSecretValue={1}", user, password);

            var factory = MessagingFactory.CreateFromConnectionString(connectionString);
            var deadLetterPath = SubscriptionClient.FormatDeadLetterPath("message-out-from-qnet", string.Format("{0}-ticket", customer_id));
            var client = factory.CreateMessageReceiver(deadLetterPath, ReceiveMode.ReceiveAndDelete);

            while (true)
            {
                var message = client.Receive();

                if (message != null)
                {
                    var ser = new DataContractSerializer(typeof(ticket));
                    var body = message.GetBody<ticket>(ser);
                    
                    Console.WriteLine(body.ticket_id);
                    File.WriteAllText(@"deadletter_" +  body.ticket_id + "_" + message.MessageId + ".txt", JsonConvert.SerializeObject(body, Formatting.Indented));
                }
            }

        }
    }
}
