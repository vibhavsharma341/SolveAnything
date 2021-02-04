package httpHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SolveExpression {

    private static final ConcurrentHashMap<String, JSONObject> query = new ConcurrentHashMap<>();
    static int currId = 0;

    private static final Logger logger = Logger.getLogger("SolveExpression");

    // parse the body
    public static JSONObject generateResponse(String body, URI requestURI) {

        JSONObject outJson = new JSONObject();

        try {
            JSONObject bodyJson = JSON.parseObject(body);
            logger.info(requestURI.getPath());
            logger.info(body);

            if ("/evaluate".equals(requestURI.getPath())) {
                getVal(bodyJson, outJson);
            } else if ("/details".equals(requestURI.getPath())){
                getQueryDetails(bodyJson, outJson);
            } else {
                outJson.put("error", "URI not supported, Please use only (evaluate/details)");
                outJson.put("status_code", 400);
            }

        } catch (JSONException e) {
            outJson.put("error", e.getMessage());
            outJson.put("status_code", 400);
        }

        return outJson;
    }

    private static void getQueryDetails(JSONObject bodyJson, JSONObject outJson) {
        if (!bodyJson.containsKey("id")) {
            outJson.put("error", "id is not provided");
            outJson.put("status_code", 404);
            return;
        }

        String id = bodyJson.getString("id");
        if (!query.containsKey(id)){
            outJson.put("error", "id is not present");
            outJson.put("status_code", 404);
            return;
        }

        outJson.putAll(query.get(id));
        outJson.put("status_code", 200);
    }

    // return the value of expression
    private static void getVal(JSONObject bodyJson, JSONObject outjson) {

        if (!bodyJson.containsKey("expression")) {
            outjson.put("error", "expression is not provided");
            outjson.put("status_code", 404);
            return;
        }

        String exp = bodyJson.getString("expression");

        // evaluating the expression using MathParser Lib
        long startTime = System.currentTimeMillis();
        Expression e = new Expression(exp);
        double v = e.calculate();
        long endTime = System.currentTimeMillis();

        outjson.put("id", ++currId);

        if (Double.isNaN(v)){
            outjson.put("status", false);
            outjson.put("reason", "expression is not valid");
            outjson.put("status_code", 400);
        } else {
            outjson.put("status", true);
            outjson.put("value", v);
            outjson.put("status_code", 200);
        }
        updateQuery(startTime, endTime, bodyJson);
    }

    // updating query queue
    private static void updateQuery(long startTime, long endTime, JSONObject bodyJson) {
        JSONObject queryDetails = new JSONObject();
        queryDetails.put("id", currId);
        queryDetails.put("time_taken_ms", endTime-startTime);
        queryDetails.putAll(bodyJson);
        query.put(String.valueOf(currId), queryDetails);
    }
}
