import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	static int successful_urls = 0;
	static int failed_urls = 0;
	static int aborted_urls = 0;
	static int jpeg = 0;
	static int png = 0;
	static int pdf = 0;
	static int gif = 0;
	static int text_html = 0;
	static int total_urls = 0;
	static HashMap<String, String> total_unique_urls = new HashMap<String, String>();
	static HashMap<String, String> total_unique_news_urls = new HashMap<String, String>();
	static HashMap<String, Integer> hash= new HashMap<String, Integer>();

	static HashMap<String, String> total_unique_non_news_urls = new HashMap<String, String>();
	static int count301 = 0;
	static int count200 = 0;
	static int count302 = 0;
	static int count503 = 0;
	static int count401 = 0;
	static int count403 = 0;
	static int count404 = 0;
	static int size1to9kb = 0;
	static int size10to99kb = 0;
	static int size100to1mb = 0;
	static int sizegreaterthan1mb = 0;
	static int lessthan1kb = 0;
	static HashMap<Integer, Integer> all_status = new HashMap<Integer, Integer>();
	static HashMap<String, Integer> all_content_types = new HashMap<String, Integer>();
	int temp = 0;
	int count = 0;
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|xml|ppt|php"+ "|mp3|mp3|zip|gz))$");
	@Override
	 public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		try(FileWriter fw = new FileWriter("urls_Latimes.csv", true);

			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))

			{ 
				total_unique_urls.put(url.getURL(), url.getURL());
			  total_urls++;
			  if(href.startsWith("http://www.latimes.com/")){
				  out.println("\""+href+"\"" + "," + "OK");
				  total_unique_news_urls.put(url.getURL(), url.getURL());
			  }
			  else{
				  out.println("\""+href+"\"" + "," + "N_OK");
				  total_unique_non_news_urls.put(url.getURL(), url.getURL());
			  }
			 out.close();

			} catch (IOException e) {

			e.printStackTrace();

			}
		
		return !FILTERS.matcher(href).matches()
		&& href.startsWith("http://www.latimes.com/");
	 }
	
		
	@Override
	protected void handlePageStatusCode(WebURL weburl, int statusCode, String statusDescription){
		hash.put("\""+weburl.getURL()+"\"", statusCode);
		
		if(all_status.containsKey(statusCode)){
			temp = all_status.get(statusCode);
			temp = temp + 1;
			all_status.put(statusCode, temp);
			System.out.print(all_status.get(statusCode));
		}else{
			all_status.put(statusCode, 1);
		}
		
		

		if(statusCode >= 200 && statusCode < 300){
			successful_urls++;
		}
		
		
		if(!(statusCode >= 200 && statusCode < 300)){
			 if(statusCode >= 300 && statusCode <= 399){
				 aborted_urls++;
			 }else if(statusCode > 399){
				 failed_urls++;
			 }
		 }
	}
		
	@Override
	public void onBeforeExit(){
	for (HashMap.Entry<String, Integer> entry : hash.entrySet()){
    
		try(FileWriter fw = new FileWriter("fetch_Latimes.csv", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(entry.getKey() + "," + entry.getValue());
			    out.close();

			} catch (IOException e) {
				e.printStackTrace();    
			}
		}
	
	try(FileWriter fw = new FileWriter("CrawlReport_Latimes.txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw))
		{
		    out.println("Name: Radhika Bhatt");
		    out.println("USC ID: 6169382505");
		    out.println("News site crawled: Latimes.com \n \n");
		    out.println("Fetch Statistics");
		    out.println("================");
		    out.println("#fetches attempted:" + hash.size());
		    out.println("#fetches succeeded:" + successful_urls);
		    out.println("#fetches aborted:" + aborted_urls);
		    out.println("#fetches failed:" + failed_urls + "\n");
		    out.println("Outgoing URLs:");
		    out.println("==============");
		    out.println("Total URLs extracted:"+total_urls);
		    out.println("#unique URLs extracted:"+total_unique_urls.size());
		    out.println("# unique URLs within News Site:"+total_unique_news_urls.size());
		    out.println("# unique URLs outside News Site:"+total_unique_non_news_urls.size());
		    out.println("Status Codes:");
		    out.println("===============");
		    
		    for (HashMap.Entry<Integer,Integer> entry : all_status.entrySet()) {
		    	  out.println(entry.getKey());
		    	  out.println(entry.getValue());
		    	}
		    out.println("File Sizes:");
		    out.println("===========");
		    out.println("< 1KB:" + lessthan1kb);
		    out.println("1KB ~ <10KB:"+ size1to9kb);
		    out.println("10KB ~ <100KB:"+ size10to99kb);
		    out.println("100KB ~ <1MB:"+ size100to1mb);
		    out.println(">= 1MB:"+ sizegreaterthan1mb);
		    out.println("Content Types:");
		    out.println("==============");
		    
		    for (HashMap.Entry<String,Integer> entry : all_content_types.entrySet()) {
		    	  out.println(entry.getKey());
		    	  out.println(entry.getValue());
		    	}
		    out.close();

		} catch (IOException e) {
		e.printStackTrace();    
		}
	}
	
	
	@Override
	 public void visit(Page page) {
	 String url = page.getWebURL().getURL();
	 System.out.println("URL: " + url);
	  
	 if(all_content_types.containsKey(page.getContentType())){
			count = all_content_types.get(page.getContentType());
			count = count + 1;
			all_content_types.put(page.getContentType(), count);
		}else{
			all_content_types.put(page.getContentType(), 1);
		}
	 
	 Integer contentdata = page.getContentData().length;
	 
	 int contentdatainmb = contentdata/1024;

	 if(contentdatainmb >0 && contentdatainmb < 1){
		 lessthan1kb++;
	 }
	 if(contentdatainmb >= 1 && contentdatainmb < 10){
		 size1to9kb++;
	 }else if(contentdatainmb >= 10 && contentdatainmb < 100){
		 size10to99kb++;
	 }else if(contentdatainmb >=100 && contentdatainmb < 1024){
		 size100to1mb++;
	 }else if(contentdatainmb >= 1024){
		 sizegreaterthan1mb++;
	 }
	 
	 
	 if (page.getParseData() instanceof HtmlParseData) {
		 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		 String text = htmlParseData.getText();
		 String html = htmlParseData.getHtml();
		 Set<WebURL> links = htmlParseData.getOutgoingUrls();
		 System.out.println("Text length: " + text.length());
		 System.out.println("Html length: " + html.length());
		 System.out.println("Number of outgoing links: " + links.size());
		 
		 System.out.print(page.getContentType().split(";")[0]);
		 System.out.print(page.getContentType().split(";")[0].equals("text/html"));
		 
		 
		 try(FileWriter fw = new FileWriter("visit_Latimes.csv", true);

				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))

				{
				        out.println("\""+url+"\"" + "," + contentdata/1024 + "KB" + "," + links.size() + "," + page.getContentType().split(";")[0]);
				        out.close();

				} catch (IOException e) {

				e.printStackTrace();

				}				 
		 
	 }else{
		 try(FileWriter fw = new FileWriter("Latimes.csv", true);

				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))

				{
				        out.println("\""+url+"\"" + "," + contentdata/1024 + "KB" + "," + "0" + "," + page.getContentType().split(";")[0]);
				        out.close();

				} catch (IOException e) {

				e.printStackTrace();

				}
	 }
	
	}
}
