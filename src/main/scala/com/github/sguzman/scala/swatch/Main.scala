package com.github.sguzman.scala.swatch

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList
import org.apache.commons.lang3.StringUtils

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val shows = cartoons
    val eps = shows.par.map(_.head).map(episodes).filter(_.isDefined).flatMap(_.get)

    val ifrms = eps.par
      .map(_.head)
      .map(iframes)
      .filter(_.isDefined)
      .map(_.get)

    val vids  = ifrms.map(videos)
    println(vids)
  }

  def videos(url: String) = {
    val request = Http(url)
    val response = request.asString

    val doc = JsoupBrowser().parseString(response.body)
    val scriptTag = doc >> elementList("""script[type="text/javascript"]""")
    if (scriptTag.isEmpty) None
    else {
      val lastScript = scriptTag.last
      val script = lastScript.innerHtml
      val pattern = """file.*""".r
      val allMatches = pattern.findAllMatchIn(script).toList.map(_.toString)
      val urls = allMatches.map(StringUtils.substringBetween(_, "\""))
      Some(urls)
    }
  }

  def iframes(url: String) = {
    try {
      val request = Http(url)
      val response = request.asString

      val doc = JsoupBrowser().parseString(response.body)
      val iframe = doc >> elementList("""iframe[id^=frame]""")

      val iframeSrc = iframe.head
      Some(iframeSrc.attr("src"))
    } catch {
      case e: Throwable =>
        Console.err.println(e.getMessage)
        None
    }
  }

  def episodes(url: String) = {
    try {
      val selector = "#catlist-listview > ul > li > a"
      val request = Http(url)

      val doc = JsoupBrowser().parseString(request.asString.body)
      val episodesElementList = doc >> elementList(selector)

      val eps = episodesElementList.map(e => List(e.attr("href"), e.text))
      Some(eps)
    } catch {
      case e: Throwable =>
        Console.err.println(e.getMessage)
        None
    }
  }

  def cartoons = {
    val url = "https://www.watchcartoononline.io/cartoon-list"
    val request = Http(url)

    val browser = JsoupBrowser()
    val doc = browser.parseString(request.asString.body)
    val shows = doc >> elementList("#ddmcc_container > div > ul > ul > li > a")

    val show = shows.map(e => List(e.attr("href"), e.text))
    show
  }
}
