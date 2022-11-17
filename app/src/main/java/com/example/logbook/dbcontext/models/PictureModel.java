package com.example.logbook.dbcontext.models;

public class PictureModel
{
    private String url;
    private Long id;

    public String getUrl()
    {
        return this.url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Long getId()
    {
        return this.id;
    }

    public PictureModel(Long id, String url)
    {
        this.id = id;
        this.url = url;
    }


}
