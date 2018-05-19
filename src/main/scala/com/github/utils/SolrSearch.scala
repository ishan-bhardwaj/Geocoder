package com.github.utils

import com.github.Geocoordinates
import org.apache.solr.client.solrj.{SolrClient, SolrQuery}
import org.apache.solr.client.solrj.impl.HttpSolrClient

class SolrSearch {

  private val solrClient: SolrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/GeoCoder").build()

  def getCoordinates(zip:String):Geocoordinates={
    val query: SolrQuery = new SolrQuery().setQuery("*:*").addFilterQuery("Zip_Code:" + zip).addField("*").setStart(0)
    val response = solrClient.query(query)
    val solrResults = response.getResults()
    val solrResultMap = solrResults.get(0).getFieldValueMap
    Geocoordinates(solrResultMap.get("Latitude").asInstanceOf[Double], solrResultMap.get("Longitude").asInstanceOf[Double])
  }

}
