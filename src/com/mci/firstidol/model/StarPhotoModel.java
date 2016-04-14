package com.mci.firstidol.model;

import java.io.Serializable;
import java.util.List;

public class StarPhotoModel implements Serializable{

	public long ArticleId;
	
	public String Ico;
	
	public boolean HasUp;
	
	public int UpCount;
	
	public String ViewCount;
	
	public int CommentCount;
	
	public String PublishDate;

	public List<PhotoModel> ArticlePictures;
}
