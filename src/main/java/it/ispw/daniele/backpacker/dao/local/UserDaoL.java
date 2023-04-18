package it.ispw.daniele.backpacker.dao.local;

import it.ispw.daniele.backpacker.dao.DaoTemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserDaoL extends DaoTemplate {

    public Boolean createUser(String username, String name, String surname,
                              String email, String password, String profilePicture) {
        return (this.execute(() -> {

            JSONObject f = readJsonFromUrl();
            System.out.println(f);
            JSONArray  arr = (JSONArray) f.get("user");
            System.out.println(arr);

            JSONObject newUser = new JSONObject().put("username", username)
                    .put("profilePicture", profilePicture)
                    .put("password", password)
                    .put("email", email)
                    .put("surname", surname)
                    .put("name", name);

            System.out.println(newUser);
            arr.put(newUser);

            System.out.println(arr);
            System.out.println("JSON" + readJsonFromUrl());

            try (FileWriter file = new FileWriter("C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json"))
            {
                file.write(f.toString());
                System.out.println("Successfully updated json object to file...!!");
            }

//            File f = new File("file.json");
//            if (f.exists()){
//                InputStream is = new FileInputStream("file.json");
//                System.out.println(JSON + is);
//                String jsonTxt = IOUtils.toString(is, "UTF-8");
//
//                System.out.println(jsonTxt);
//                JSONObject json = new JSONObject(jsonTxt);
//                String a = json.getString("1000");
//                System.out.println(a);
//
//                JSONParser parser = new JSONParser();
//            Object obj = parser.parse(new FileReader("c:\\file.json"));
//
//            JSONObject jsonObject =  (JSONObject) obj;
//
//            JSONObject obj = new JSONObject(jsonStr);
//
//            JSONArray arr = obj.getJSONArray("carTypes");
//
//            JSONObject hondaCivic17Json = new JSONObject()
//                    .put("model", "civic")
//                    .put("make", "honda")
//                    .put("year", "2017");
//
//            JSONArray newArray = arr.put(hondaCivic17Json);
//            JSONObject newJson = obj.put("carTypes", newArray);
//
//            String newJsonStr = newJson.toString();
//            System.out.println(newJsonStr);


//            Connection con = DatabaseLoginConnection.getLoginConnection();
//
//            String sql = "call backpacker.add_user(?, ?, ?, ?, ?, ?);\r\n";
//            try (PreparedStatement stm = con.prepareStatement(sql)) {
//                stm.setString(1, username);
//                stm.setString(2, name);
//                stm.setString(3, surname);
//                stm.setString(4, email);
//                stm.setString(5, password);
//                stm.setString(6, profilePicture);
//                stm.executeUpdate();
//            }
            return true;
        }) != null);

    }


        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }


        public static JSONObject readJsonFromUrl() throws IOException, JSONException {
            try (InputStream is = new FileInputStream("C:/Users/danie/Desktop/Backpacker/src/main/resources/localDB/user.json");) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONObject(jsonText);
            }
        }

}
