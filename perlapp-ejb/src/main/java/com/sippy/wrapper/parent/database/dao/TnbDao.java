package com.sippy.wrapper.parent.database.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TnbDao {

  @Id String tnb;

  String name;

  Boolean isTnb = false;

  public String getTnb() {
    return tnb;
  }

  public void setTnb(String tnb) {
    this.tnb = tnb;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIsTnb() { return isTnb; }

  public void setTnb(Boolean tnb) {
    isTnb = tnb;
  }
}
