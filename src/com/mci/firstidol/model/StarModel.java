package com.mci.firstidol.model;

import java.io.Serializable;

import cn.shinsoft.Model;
import cn.shinsoft.annotation.Column;
import cn.shinsoft.annotation.Table;
/**
 * 明星对象
 * @author wanghaixiao
 *
 */
@Table(name="starModel")
public class StarModel extends Model implements Serializable{

	@Column(name="starId")
	public int starId;//id
	
	@Column(name="StarName")
	public String StarName;//名称
	
	@Column(name="Avatar")
	public String Avatar;//头像
	
	@Column(name="BgImage")
	public String BgImage;//背景图
	
	@Column(name="CreateDate")
	public String CreateDate;//创建时间
	
	@Column(name="Description")
	public String Description;//描述
	
	@Column(name="FollowCount")
	public int FollowCount;//粉丝数量
	
	@Column(name="MagazineId")
	public int MagazineId;//刊物ID
	
	@Column(name="IsCheckIn")
	public boolean IsCheckIn;//是否有观察团
	
	public boolean has_selected = false;//是否已经被选中
	
}
