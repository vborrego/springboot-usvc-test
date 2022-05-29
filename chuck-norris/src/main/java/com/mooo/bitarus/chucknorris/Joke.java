package com.mooo.bitarus.chucknorris;;
import com.google.gson.annotations.SerializedName;

public class Joke{
 
    @SerializedName("categories")
    private String[] categories;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("icon_url")
    private String iconUrl;
    @SerializedName("id")
    private String id;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("url")
    private String url;
    @SerializedName("value")
    private String value;    

    public String[] getCategories(){
        return this.categories;
    }

    public String getCreatedAt(){        
        return this.createdAt;
    }

    public String getIconUrl(){
        return this.iconUrl;        
    }

    public String getId(){   
        return this.id;     
    }

    public String getUpdatedAt(){        
        return this.updatedAt;
    }

    public String getUrl(){    
        return this.url;    
    }

    public String getValue(){
        return this.value;
    }

    public void setCategories(String[] categories){
        this.categories = categories;
    }

    public void setCreatedAt(String createdAt ){        
        this.createdAt = createdAt;
    }

    public void setIconUrl(String iconUrl){
        this.iconUrl=iconUrl;        
    }

    public void setId(String id){   
        this.id=id;     
    }

    public void setUpdatedAt(String updatedAt){        
        this.updatedAt=updatedAt;
    }

    public void setUrl(String url){    
        this.url=url;    
    }

    public void setValue(String value){
        this.value=value;
    }
}