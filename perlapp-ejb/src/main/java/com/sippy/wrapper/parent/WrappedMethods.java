package com.sippy.wrapper.parent;

import com.sippy.wrapper.parent.database.DatabaseConnection;
import com.sippy.wrapper.parent.database.dao.TnbDao;
import com.sippy.wrapper.parent.request.GetTnbListRequest;
import com.sippy.wrapper.parent.request.JavaTestRequest;
import com.sippy.wrapper.parent.response.GetTnbListResponse;
import com.sippy.wrapper.parent.response.JavaTestResponse;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class WrappedMethods {

  private static final Logger LOGGER = LoggerFactory.getLogger(WrappedMethods.class);
  private static final String EXCLUDED_TNB_NUMBERS_REGEX = "D146|D218|D248";

  @EJB DatabaseConnection databaseConnection;

  @RpcMethod(name = "getTnbList")
  public GetTnbListResponse getTnbList(GetTnbListRequest request) {
    List<TnbDao> tnbs = new ArrayList<>();
    String faultCode = "200";
    String faultString = "Method success";

    try {
      LOGGER.info("Fetching TNB list from the database");
      List<TnbDao> allTnbs = databaseConnection.getAllTnbs();
      allTnbs.addAll(getDefaultTnbs());

      tnbs.addAll(
        allTnbs.stream()
          .filter((tnbDao -> !tnbDao.getTnb().matches(EXCLUDED_TNB_NUMBERS_REGEX)))
          .peek(tnbDao -> {
            if(tnbDao.getTnb() != null) {
              tnbDao.setTnb(tnbDao.getTnb().equals(request.number()));
            }
          })
          .sorted(Comparator.comparing(tnb -> tnb.getName().toLowerCase()))
          .toList()
      );
    } catch (Exception e) {
      LOGGER.error("Error fetching TNB list", e);
      faultCode = "500";
      faultString = "Database error: " + e.getMessage();
    }
    return new GetTnbListResponse(faultCode, faultString, tnbs);
  }

  private List<TnbDao> getDefaultTnbs() {
    TnbDao deutscheTelekom = new TnbDao();
    deutscheTelekom.setTnb("D001");
    deutscheTelekom.setName("Deutsche Telekom");
    return List.of(deutscheTelekom);
  }

  @RpcMethod(name = "javaTest", description = "Check if everything works :)")
  public Map<String, Object> javaTest(JavaTestRequest request) {
    JavaTestResponse response = new JavaTestResponse();

    int count = databaseConnection.getAllTnbs().size();

    LOGGER.info("the count is: " + count);

    response.setId(request.getId());
    String tempFeeling = request.isTemperatureOver20Degree() ? "warm" : "cold";
    response.setOutput(
        String.format(
            "%s has a rather %s day. And he has %d tnbs", request.getName(), tempFeeling, count));

    Map<String, Object> jsonResponse = new HashMap<>();
    jsonResponse.put("faultCode", "200");
    jsonResponse.put("faultString", "Method success");
    jsonResponse.put("something", response);

    return jsonResponse;
  }
}
