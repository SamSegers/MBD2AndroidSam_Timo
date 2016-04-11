package nl.samtimo.pubcrawl.request;

import nl.samtimo.pubcrawl.request.RequestMethod;
import nl.samtimo.pubcrawl.request.RequestProperty;

/**
 * Created by admin on 25-03-16.
 */
public class Request {
    private RequestMethod method;
    private String path;
    private String parameters;
    private RequestProperty[] properties;

    public Request(RequestMethod method, String path, String parameters, RequestProperty[] properties){
        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.properties = properties;
    }

    public RequestMethod getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }

    public String getParameters(){
        return parameters;
    }

    public RequestProperty[] getProperties(){
        return properties;
    }
}
