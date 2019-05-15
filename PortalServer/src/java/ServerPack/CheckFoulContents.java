/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import java.util.StringTokenizer;
import java.util.Vector;

public class CheckFoulContents {

    static void pre_Process() {
        PostMessageServlet.processedTokens = new Vector<String>();
        // System.out.println("Input: " + inpComment);
        try {
            StringTokenizer st = new StringTokenizer(PostMessageServlet.inpComment.toLowerCase(), PostMessageServlet.delim);
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim().toLowerCase();
                System.out.println("Found Token: " + token);
                if (!(token.equals(""))) {
                    if (check_Key_Word(token)) {
                        PostMessageServlet.polarity = 1;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean check_Key_Word(String token) {
        boolean flag = false;

        try {
            for (int i = 0; i < PostMessageServlet.vctFoulWord.size(); i++) {
                if (token.equalsIgnoreCase(PostMessageServlet.vctFoulWord.get(i))) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
