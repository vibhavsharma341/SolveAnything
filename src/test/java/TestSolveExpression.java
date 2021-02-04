import com.alibaba.fastjson.JSONObject;
import httpHandler.SolveExpression;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TestSolveExpression {

    @Test
    public void testSolveExpression() throws URISyntaxException {
        JSONObject body = new JSONObject();
        body.put("expression", "1 + 2 + (7 - 6) * 10");
        JSONObject response = SolveExpression.generateResponse(body.toJSONString(), new URI("/evaluate"));
        Assert.assertEquals("13.0", response.getString("value"));
    }
}
