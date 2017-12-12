package com.example.lucas.whogoesthere;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/*
    To use, simply run "Test in Order."
    It will fail if some tests are called out of order.
 */
public class TestScripts {
    OkHttpClient client = new OkHttpClient();

    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response r = client.newCall(request).execute();
        if(r.isSuccessful()) {
            String resp = r.body().string();
            System.out.println(resp);
            return resp;
        }
        return "";
    }

    @Test
    public void TestInOrder() throws IOException, JSONException {
        ATestLogin();
        BTestGroupsView();
        CTestStatsView();
        DTestCreateGroup();
        ETestCreateStat();
        FTestEditGroup();
        GTestEditStat();
        HTestRemoveStat();
        ITestRemoveGroup();
        return;
    }

    public void ATestLogin() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/login_for_client.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void BTestGroupsView() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/get_groups.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void CTestStatsView() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/get_stats.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "TEST");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void DTestCreateGroup() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/add_group.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "forunittest");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void ETestCreateStat() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/add_stat.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "forunittest");
        urlBuilder.addQueryParameter("firstName", "Jenkins");
        urlBuilder.addQueryParameter("lastName", "Foster");
        urlBuilder.addQueryParameter("role", "testing");
        urlBuilder.addQueryParameter("day", "20171225");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void FTestEditGroup() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/update_group.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "forunittest");
        urlBuilder.addQueryParameter("newgroupname", "altforunittest");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void GTestEditStat() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/update_stat.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "altforunittest");
        urlBuilder.addQueryParameter("firstName", "Jenkins");
        urlBuilder.addQueryParameter("lastName", "Foster");
        urlBuilder.addQueryParameter("role", "testing");
        urlBuilder.addQueryParameter("day", "20171225");
        urlBuilder.addQueryParameter("newfirstName", "Jimmy");
        urlBuilder.addQueryParameter("newlastName", "Jinkins");
        urlBuilder.addQueryParameter("newrole", "moretesting");
        urlBuilder.addQueryParameter("newday", "20171224");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void HTestRemoveStat() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/remove_stat.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "altforunittest");
        urlBuilder.addQueryParameter("firstName", "Jimmy");
        urlBuilder.addQueryParameter("lastName", "Jinkins");
        urlBuilder.addQueryParameter("role", "moretesting");
        urlBuilder.addQueryParameter("day", "20171224");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }

    public void ITestRemoveGroup() throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://adm-store.com/AttendanceDB/remove_group.php").newBuilder();
        urlBuilder.addQueryParameter("username", "Bob");
        urlBuilder.addQueryParameter("password", "BARRY");
        urlBuilder.addQueryParameter("groupname", "altforunittest");
        String built = urlBuilder.build().toString();
        String resp = doGetRequest(built);
        JSONObject obj = new JSONObject(resp);
        int success = obj.getInt("success");
        assertEquals(success, 1);
    }
}