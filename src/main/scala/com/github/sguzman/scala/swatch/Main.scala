package com.github.sguzman.scala.swatch

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val shows = cartoons

    val episodes = shows.par.map(t => {
      val selector = "#catlist-listview > ul > li > a"
      val request = Http(t(1))
      val doc = JsoupBrowser().parseString(request.asString.body)
      val episodesElementList = doc >> elementList(selector)
      val episodes = episodesElementList.map(e => List(e.text, e.attr("href")))
      episodes foreach (e => println(s"\tGot episode $e"))
      List(t, episodes)
    })

    println(episodes)
  }

  def cartoons = {
    val url = "https://www.watchcartoononline.io/cartoon-list"
    val request = Http(url)

    val browser = JsoupBrowser()
    val doc = browser.parseString(request.asString.body)
    val shows = doc >> elementList("#ddmcc_container > div > ul > ul > li > a")

    val show = shows.map(e => List(e.text, e.attr("href")))
    print(s"Got show ${show.head}")

    show
  }
}
