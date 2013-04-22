package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.BookMeta;
import digimax.structural.ApplicationRuntimeException;
import java.nio.charset.Charset;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/18/13
 * Time: 12:39 PM
 */
public class BookMetaServiceImpl implements BookMetaService {

    //GOOGLE_BOOK_API_KEY=AIzaSyD_gFyi2tQQs6522D1_V2aBDLChzEkw0xw
    //https://www.googleapis.com/books/v1/volumes?q=isbn:0330358448
    //https://www.googleapis.com/books/v1/volumes?q=isbn:053105988X
    //    https://isbndb.com/api/books.xml?access_key=IP3U2HMG&index1=book_id&value1=jesus_incident
    //    https://isbndb.com/api/books.xml?access_key=IP3U2HMG&index1=combined&value1=jesus_incident

    private static final String GOOGLE_BOOK_KEY = System.getProperty("digimax.ncapsuld.google.book.key")!=null?
            System.getProperty("digimax.ncapsuld.google.book.key") : "IAIzaSyD_gFyi2tQQs6522D1_V2aBDLChzEkw0xw";
    private static final String ISBNDB_KEY = System.getProperty("digimax.ncapsuld.isbndb.key")!=null?
            System.getProperty("digimax.ncapsuld.isbndb.key") : "IP3U2HMG";
    private static final String GOOGLE_BOOK_URL =
            "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private static final String ISBNDB_UNIQUE_BOOK_URL =
            "https://isbndb.com/api/books.xml?access_key="+ISBNDB_KEY+"&index1=book_id&value1=";

    private static final int WEB_SERVICE_TIMEOUT_THRESHOLD = 1000;

    private static final ContentType TEXT_UTF8_XML = ContentType.create(
            "text/xml", Consts.UTF_8);

    @Inject
    private Logger logger;

    @Inject
    private Session session;

    public void save(BookMeta bookMeta) {
        session.save(bookMeta);
    }

    public void delete(BookMeta bookMeta) {
        session.delete(bookMeta);
    }

    public void populateBookMeta(Book book) {
        BookMeta bookMeta = book.bookMeta;
        if (bookMeta==null) {
            bookMeta = new BookMeta();
            book.bookMeta = bookMeta;
            bookMeta.book = book;
        }
        String searchTitle = book.title.replace(' ', '_').replaceAll("`","");
        String isbndb_unique_book_url = ISBNDB_UNIQUE_BOOK_URL+searchTitle;

        logger.debug("isbndb_unique_book_url :: {}", isbndb_unique_book_url);

        //get meta info from ISBNDB
        String xmlPath = null;
        try {
//            HttpResponse httpResponse = Request.Get(isbndb_unique_book_url)
//                    .execute().returnResponse();//  returnContent();
//            logger.debug("ISBNDB returned HttpResponse :: {}", httpResponse);
            Document document = getDocument(isbndb_unique_book_url);
            logger.debug("ISBNDB returned Document :: {}", document);

            //Evaluate XPath against Document itself
            XPath xPath = XPathFactory.newInstance().newXPath();
            xmlPath = "/ISBNdb";
            NodeList nodes = (NodeList)xPath.evaluate( xmlPath,
                    document.getDocumentElement(), XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); ++i) {
                Node node = nodes.item(i);
                NamedNodeMap nodeMap = node.getAttributes();
                Node serverTime = nodeMap.getNamedItem("server_time");
                logger.debug("Element #{} :: {}", i+"."+ node, serverTime);
                NodeList childNodes = node.getChildNodes();

                for (int j=0; j<childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    logger.debug("childNode #{} :: {}", j+"."+childNode, childNode.getNodeValue());
                }

                Node bookListNode = node.getFirstChild().getNextSibling();
                if (bookListNode.getChildNodes().getLength()<=1) {
                    break;
                }
                Node bookDataNode = bookListNode.getFirstChild().getNextSibling();
                NamedNodeMap bookDataNodeMap = bookDataNode.getAttributes();
                Node bookIdNode = bookDataNodeMap.getNamedItem("book_id");
                Node isbnNode = bookDataNodeMap.getNamedItem("isbn");
                Node isbn13Node = bookDataNodeMap.getNamedItem("isbn13");
                logger.debug("  Book Id :: {}", bookIdNode);
                logger.debug("  Book ISBN :: {}", isbnNode);
                logger.debug("  Book ISBN13 :: {}", isbn13Node);
                bookMeta.isbn = isbnNode.getNodeValue();
                bookMeta.isbn13 = isbn13Node.getNodeValue();

                Node titleNode = bookDataNode.getFirstChild().getNextSibling().getFirstChild();
                logger.debug("  Book Title :: {}", titleNode);
                bookMeta.title = titleNode.getTextContent();
                logger.debug("  BookMeta Title :: {}", bookMeta.title);

                Node longTitleNode = bookDataNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
                bookMeta.longTitle = longTitleNode.getTextContent();
                logger.debug("  BookMeta LongTitle :: {}", bookMeta.longTitle);

                Node authorNamesNode = bookDataNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
                logger.debug("  Book AuthorNames :: {}", authorNamesNode);
                bookMeta.authorNames = authorNamesNode.getTextContent();
                logger.debug("  BookMeta AuthorNames :: {}", bookMeta.authorNames);

                Node publisherNode = bookDataNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
                logger.debug("  Book Publisher :: {}", publisherNode);
                bookMeta.publisherName = publisherNode.getTextContent();
                logger.debug("  BookMeta Publisher :: {}", bookMeta.publisherName);

                //Get metainfo from Google Books
                String google_book_url = GOOGLE_BOOK_URL+bookMeta.isbn;
                HttpResponse httpResponse = Request.Get(google_book_url)
                    .execute().returnResponse();//.returnContent();
                StatusLine statusLine = httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(
                            statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                String jsonText = EntityUtils.toString(entity);
                logger.debug("Google returned JSON :: {}", jsonText);
                JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonText);
                if (json.containsKey("items")) {
                    try {
                        JSONArray items = json.getJSONArray("items");
                        logger.debug("Google Books items :: {}", items);
                        for (int j=0; j<items.size(); j++) {
                            JSONObject item = items.getJSONObject(j);
                            if (item.containsKey("volumeInfo")) {
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                                if (volumeInfo.containsKey("description")) {
                                    String description = volumeInfo.getString("description");
                                    bookMeta.description = description;
                                }
                                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                if (imageLinks!=null && imageLinks.containsKey("thumbnail")) {
                                    String thumbnailUrl = imageLinks.getString("thumbnail");
                                    logger.debug("Google Books thumbnail :: {}", thumbnailUrl);
                                    bookMeta.thumbnailUrl = thumbnailUrl;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        logger.debug("Google Books has no info for :: {}", book.title);
                    }

                }
            }
            save(bookMeta);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(
                    String.format("ISBNDB Request Error for Book URL :: %s", isbndb_unique_book_url), e);
        } catch (XPathExpressionException e) {
            throw new ApplicationRuntimeException(
                    String.format("xPath NodeList evaluation Error for :: %s", xmlPath), e);
        }

    }

    private Document getDocument(String webServiceUrl) throws IOException {
        Document result = Request.Get(webServiceUrl).connectTimeout(WEB_SERVICE_TIMEOUT_THRESHOLD)
                .socketTimeout(WEB_SERVICE_TIMEOUT_THRESHOLD)
                .execute().handleResponse(new ResponseHandler<Document>() {

                    public Document handleResponse(final HttpResponse response) throws IOException {
                        StatusLine statusLine = response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        if (statusLine.getStatusCode() >= 300) {
                            throw new HttpResponseException(
                                    statusLine.getStatusCode(),
                                    statusLine.getReasonPhrase());
                        }
                        if (entity == null) {
                            throw new ClientProtocolException("Response contains no content");
                        }
                        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                        try {
                            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                            ContentType contentType = ContentType.getOrDefault(entity);
                            if (!contentType.toString().equals(TEXT_UTF8_XML.toString())) {
                                throw new ClientProtocolException("Unexpected content type:" + contentType);
                            }
                            Charset charset = contentType.getCharset();
                            String charsetName = charset.name();
                            if (charset == null || charsetName == null) {
                                charsetName = HTTP.DEFAULT_CONTENT_CHARSET;
                            }
                            return docBuilder.parse(entity.getContent(), charsetName);
                        } catch (ParserConfigurationException ex) {
                            throw new IllegalStateException(ex);
                        } catch (SAXException ex) {
                            throw new ClientProtocolException("Malformed XML document", ex);
                        }
                    }

                });
        return result;
    }


//            String xmlPath = "/html/body/p/div[3]/a";
//    <html>
//    <head>
//    </head>
//    <body>
//    <p>
//    <div></div>
//    <div></div>
//    <div><a>link</a></div>
//    </p>
//    </body>
//    </html>

}

