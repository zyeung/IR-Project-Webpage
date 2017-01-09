package pageDAO;



/**
 * Page entity. @author MyEclipse Persistence Tools
 */

public class Page  implements java.io.Serializable {


    // Fields    
     private Integer urlId;
     private String url;
     private double tfidf;


    // Constructors

    /** default constructor */
    public Page() {
    }

    
    /** full constructor */
    public Page(int urlId, String url, double tfidf) {
        this.urlId = urlId;
        this.url = url;
        this.tfidf = tfidf;
    }

   
    // Property accessors

    public Integer getUrlId() {
		return urlId;
	}


	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public double getTfidf() {
		return tfidf;
	}


	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}



}