package search;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pageDAO.Page;

import java.util.*;



public class SearchServlet extends HttpServlet {
	private static String TFIDF_LOCATION = "";
	private static String URL_LOCATION = "";
	private static Map<String, Map<Integer, Double>>mapTFIDF = deserializeMap(TFIDF_LOCATION);
	private static List<String> urlList = deserializeList();
	
	private static Tokenizer myTokenizer = new Tokenizer();
	
	public static Map deserializeMap(String mapLocation){
		try {
			FileInputStream fis = new FileInputStream(mapLocation);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Map map = (HashMap) ois.readObject();
			System.out.println("Map loaded!" + mapLocation);
			ois.close();
			fis.close();
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.print("Failed to read map");
		return null;	// error
	}

	public static List deserializeList(){
		try {
			FileInputStream fis = new FileInputStream(URL_LOCATION);
			ObjectInputStream ois = new ObjectInputStream(fis);
			List list = (List) ois.readObject();
			ois.close();
			fis.close();
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.print("Failed to read URL_List");
		return null;	// error
	}
		

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// result map: store urlId, Page. Each page contains the accumulated tfidf
		Map<Integer, Page> resultMap = new HashMap<Integer, Page> ();	
		
		String query = request.getParameter("query").toLowerCase();
		System.out.println("Get query: " + query);				// Console testing if query is received from index.jsp 

		// tokenize query into terms
		List<String> terms = myTokenizer.tokenizeSingleText(query);
		
		
		for(String term: terms){
			System.out.println("Tokenized term: " + term);
			// get the urls that contains the term
			Map<Integer, Double> termTFIDFs = mapTFIDF.get(term);
			if(termTFIDFs == null){								// if the term is not found, skip this term
				continue;
			}
			for(int urlId: termTFIDFs.keySet()){
				if(!resultMap.containsKey(urlId)){		// if this document is not in the result map, create a new entry
					Page tmpPage = new Page(urlId, urlList.get(urlId), termTFIDFs.get(urlId));
					resultMap.put(urlId, tmpPage);
				}
				else{									// if this document is in the result map, update its tfidf
					double old_tfidf = resultMap.get(urlId).getTfidf();
					resultMap.get(urlId).setTfidf(old_tfidf + termTFIDFs.get(urlId));
				}				
			}
			
		}
		
		
		
		List<Page> pageList = new ArrayList<Page> ();
		if(resultMap.size() != 0){
			for(Page tmpPage: resultMap.values()){
				pageList.add(tmpPage);
			}
		}
		
		Collections.sort(pageList, new Comparator<Page>(){
										public int compare(Page p1, Page p2){
											double tfidf1 = p1.getTfidf();
											double tfidf2 = p2.getTfidf();
											return -1*Double.compare(tfidf1, tfidf2);
										}
									}				
		);	
		
		HttpSession session  = request.getSession();
		session.setAttribute("query", query);	
		session.setAttribute("pageCnt", pageList.size());
		session.setAttribute("pageList", pageList);		
		response.sendRedirect("display.jsp");

	}
	
	
	
	
	public Map<String,Map<Integer,Double>> readTFIDFJson() throws FileNotFoundException {		
		System.out.println("Read json into memory...");
        InputStream is = new FileInputStream(TFIDF_LOCATION);
        Reader reader = new InputStreamReader(is);
        Type fooType = new TypeToken<Map<String,Map<Integer,Double>>>() {}.getType();
        Gson gson=new Gson();
        System.out.println("Start reading Json");

        Map<String,Map<Integer,Double>> newMap=gson.fromJson(reader,fooType);
        System.out.println("Read json Done");
        //printReadedTFIDF(newMap);
        return newMap;

    }

    public void printReadedTFIDF(Map<String,Map<Integer,Double>> newMap){

        Iterator it = newMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            String word= (String) pair.getKey();

            Map<Integer,Double> TFIDFmap= (Map<Integer, Double>) pair.getValue();

            Iterator it1 = TFIDFmap.entrySet().iterator();

            while (it1.hasNext()) {

                Map.Entry tfidfPair = (Map.Entry)it1.next();

                Integer urlID= (Integer) tfidfPair.getKey();

                double tfidf= (Double) tfidfPair.getValue();

                System.out.println("The word["+word+"] in docID "+urlID+" TFIDF is "+tfidf);

            }

        }

    }
	
	
	

}
