package com.github.sguzman.scala.swatch

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val shows = cartoons

    println(shows)
  }

  def cartoons: List[(String, String)] = {
    val url = "https://www.watchcartoononline.io/cartoon-list"
    val request = Http(url)
    val response = request.asString

    val browser = JsoupBrowser()
    val doc = browser.parseString(response.body)
    val shows = doc >> elementList("#ddmcc_container > div > ul > ul > li > a")

    val tuple = shows.map(e => (e.text, e.attr("href")))
    tuple
  }
}
