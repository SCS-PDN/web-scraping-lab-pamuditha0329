public class ScrapeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getParameter("url");
        String[] options = request.getParameterValues("options");

        List<String> scrapedData = WebScraper.scrape(url, options);

        // Session tracking
        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) visitCount = 0;
        session.setAttribute("visitCount", ++visitCount);

        // Convert to JSON
        Gson gson = new Gson();
        String json = gson.toJson(scrapedData);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
