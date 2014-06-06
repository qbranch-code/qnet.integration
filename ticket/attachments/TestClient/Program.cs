using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace TestClient
{
    internal class Program
    {
        // You should change these
        private const string apiKey = "";
        private const string customerId = "";
        private const string ticketId = "";
        private const string attachmentId = "";

        private static void Main(string[] args)
        {
            var downloadResult = Download().Result;
            Console.WriteLine(downloadResult);

            var uploadResult = Upload().Result;
            Console.WriteLine(uploadResult);

            Console.WriteLine("Done");
            Console.ReadLine();
        }

        private static async Task<HttpStatusCode> Download()
        {
            var apiUri = string.Format("https://api-dev.qbranch.se/api/customers/{0}/tickets/{1}/attachments/{2}",
                                       customerId, ticketId, attachmentId);

            var message = new HttpRequestMessage();
            message.RequestUri = new Uri(apiUri);
            message.Headers.Add("qnet-api-key", apiKey);
            message.Method = HttpMethod.Get;

            using (var client = new HttpClient())
            {
                var response = await client.SendAsync(message);
                var httpStream = await response.Content.ReadAsStreamAsync();
                var fileStream =
                    new FileStream(@"c:\temp\download\" + response.Content.Headers.ContentDisposition.FileNameStar,
                                   FileMode.Create, FileAccess.Write);

                await httpStream.CopyToAsync(fileStream);

                return response.StatusCode;
            }
        }

        private static async Task<HttpStatusCode> Upload()
        {
            var files = new List<string>();

            files.Add(@"c:\temp\midi.xml");
            files.Add(@"c:\temp\maxi.xml");
            files.Add(@"c:\temp\efti.xml");

            var apiUri = string.Format("https://api-dev.qbranch.se/api/customers/{0}/tickets/{1}/attachments",
                                       customerId, ticketId);

            var message = new HttpRequestMessage();
            message.RequestUri = new Uri(apiUri);
            message.Headers.Add("qnet-api-key", apiKey);
            message.Method = HttpMethod.Post;

            var content = new MultipartFormDataContent();
            foreach (var file in files)
            {
                var filestream = new FileStream(file, FileMode.Open);
                var fileName = Path.GetFileName(file);
                content.Add(new StreamContent(filestream), "file", fileName);
            }

            message.Content = content;

            using (var client = new HttpClient())
            {
                var response = await client.SendAsync(message);
                return response.StatusCode;
            }
        }
    }
}
