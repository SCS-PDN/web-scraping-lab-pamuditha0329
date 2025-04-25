public class WebScraper {
    public static List<String> scrape(String url, String[] options) throws IOException {
        List<String> results = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        for (String opt : options) {
            switch (opt) {
                case "title":
                    results.add("Title: " + doc.title());
                    for (int i = 1; i <= 6; i++) {
                        Elements headings = doc.select("h" + i);
                        for (Element heading : headings) {
                            results.add("H" + i + ": " + heading.text());
                        }
                    }
                    break;
                case "links":
                    for (Element link : doc.select("a[href]")) {
                        results.add("Link: " + link.absUrl("href"));
                    }
                    break;
                case "images":
                    for (Element img : doc.select("img")) {
                        results.add("Image: " + img.absUrl("src"));
                    }
                    break;
            }
        }
        return results;
    }

    public static class NewsArticle {
        public String headline;
        public String date;
        public String author;

        public NewsArticle(String headline, String date, String author) {
            this.headline = headline;
            this.date = date;
            this.author = author;
        }
    }

    public static List<NewsArticle> scrapeNews(String url) throws IOException {
        List<NewsArticle> articles = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements articleLinks = doc.select("a[href]");

        for (Element link : articleLinks) {
            String articleUrl = link.absUrl("href");
            try {
                Document articlePage = Jsoup.connect(articleUrl).get();
                String title = articlePage.title();
                String date = articlePage.select("time").attr("datetime");
                String author = articlePage.select(".byline__name").text();
                if (!title.isEmpty()) {
                    articles.add(new NewsArticle(title, date, author));
                }
            } catch (Exception e) {
                // Skip invalid articles
            }
        }

        return articles;
    }
}
