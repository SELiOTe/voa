package club.seliote.voa.GlobalUtils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FromUrlRequestString extends AsyncTask<FromUrlRequestString.RequestInfo, Integer, String> {

    private String result = "";
    private RequestInfo mRequestInfo;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (result == null) {
            Toast.makeText(GlobalContextApplication.getContext(), "网络连接错误", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected String doInBackground(RequestInfo... requestInfos) {
        mRequestInfo = requestInfos[0];
        if (this.getRequestMethod() == ConstantValue.GET_METHOD) {
            return  doGet();
        } else {
            return doPost();
        }
    }

    @Nullable
    private URL getUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException exp) {
            LogToFile.e("convert usl string to url, but throw a MalformedURLException");
        }
        return null;
    }

    private String getRequestMethod() {
        return mRequestInfo.mMethodString == ConstantValue.GET_METHOD ? ConstantValue.GET_METHOD : ConstantValue.POST_METHOD;
    }

    private String getArgumentsString() {
        Map<String, String> args = mRequestInfo.getArgumentsMap();
        String argumentString = "";
        if (args == null || args.isEmpty()) {
            return argumentString;
        }else {
            argumentString = "?";
            for (Map.Entry<String, String> entry : args.entrySet()) {
                argumentString += entry.getKey() + "=" + entry.getValue() + "&";
            }
            argumentString = argumentString.substring(0, argumentString.length() - 1);
            return argumentString;
        }
    }

    @Nullable
    private String doGet() {
        URL url = this.getUrl(mRequestInfo.getUrlString() + this.getArgumentsString());
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(ConstantValue.GET_METHOD);
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(8000);
            try (InputStream inputStream = httpURLConnection.getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
            }
        } catch (IOException exp) {
            LogToFile.e("(GET method) get data from website error, URL String is:" + mRequestInfo.getUrlString());
        }
        return result;
    }

    private String doPost() {
        URL url = this.getUrl(mRequestInfo.getUrlString());
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(getArgumentsString());
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(8000);
            try (InputStream inputStream = httpURLConnection.getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
            }
        } catch (IOException exp) {
            LogToFile.e("(POST method) get data from website error, URL String is:" + mRequestInfo.getUrlString());
        }
        return result;
    }

    public static class RequestInfo {

        private String mUrlString;
        private String mMethodString;
        private Map<String, String> mArgumentsMap;

        public RequestInfo(String urlString, String method, @Nullable Map<String, String> arguments) {
            mUrlString = urlString;
            mMethodString = method;
            mArgumentsMap = arguments;
        }

        public String getUrlString() {
            return mUrlString;
        }

        public void setUrlString(String urlString) {
            mUrlString = urlString;
        }

        public String getMethodString() {
            return mMethodString;
        }

        public void setMethodString(String methodString) {
            mMethodString = methodString;
        }

        public Map<String, String> getArgumentsMap() {
            return mArgumentsMap;
        }

        public void setArgumentsMap(Map<String, String> argumentsMap) {
            mArgumentsMap = argumentsMap;
        }
    }

}
