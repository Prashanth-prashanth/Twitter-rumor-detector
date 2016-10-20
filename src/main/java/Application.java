import app.controller.AnnotationController;
import app.controller.IndexController;
import app.controller.KeywordCrawlerController;
import app.controller.RumorCrawlerController;
import app.twitter.TwitterHandler;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;
import twitter4j.TwitterException;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

/**
 * Entry point of the application
 * @author Fran Lozano
 */
public class Application {

        public static void main(String[] args) throws TwitterException {

            // Configure Spark
            staticFiles.location("/public");
            staticFiles.expireTime(600L);
            port(4567);
            enableDebugScreen();

            //Initialize controllers
            TwitterHandler th = new TwitterHandler();
            AnnotationController.start(th);
            KeywordCrawlerController.start(th);
            RumorCrawlerController.start(th);

            // Set up before-filters (called before each get/post)
            before("*",                  Filters.addTrailingSlashes);
            before("*",                  Filters.handleLocaleChange);

            // Set up routes
            get(Path.Web.INDEX,          IndexController.servePage);
            get(Path.Web.ANNOTATION,     AnnotationController.servePage);
            get(Path.Web.KEYWORD_CRAWLER,KeywordCrawlerController.servePage);
            get(Path.Web.RUMOR_CRAWLER,  RumorCrawlerController.servePage);
            post(Path.Web.ANNOTATION,    AnnotationController.processRequest);
            get("*",                     ViewUtil.notFound);


        }
}

