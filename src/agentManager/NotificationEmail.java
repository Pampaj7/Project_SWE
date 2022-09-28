package agentManager;

import org.javatuples.Pair;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public final class NotificationEmail implements Observer {

    @Override
    public void update(Object obj) {
        Operation o = (Operation) obj;
        String to = "";
        for (User u : Program.getInstance().getUsers()) {
            if (u instanceof Dentist)
                to += u.getEmail() + ",";
        }
        to = to.substring(0, to.length() - 1); //TODO mettere email dell'admin

        String products = "";
        for (Pair<Article, Integer> a : o.getRows()) {
            products += "--" + a.getValue0().getName() + " qta: " + a.getValue1() + " <br>"; //TODO WOWOWOWOW
        }

        String text;
        text = "<!doctype html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <title>Simple Email HTML</title>\n" +
                "\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Montserrat:400,700|Poppins:400,700\" rel=\"stylesheet\">\n" +
                "\n" +
                "    <style>\n" +
                "        /* -------------------------------------\n" +
                "              GLOBAL RESETS\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        /*All the styling goes here*/\n" +
                "\n" +
                "        img {\n" +
                "            border: none;\n" +
                "            -ms-interpolation-mode: bicubic;\n" +
                "            max-width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            background-color: #dbdbdb;\n" +
                "            font-family: 'Poppins', 'Helvetica', sans-serif;\n" +
                "            -webkit-font-smoothing: antialiased;\n" +
                "            font-size: 15px;\n" +
                "            line-height: 28px;\n" +
                "            letter-spacing: .5px;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            -ms-text-size-adjust: 100%;\n" +
                "            -webkit-text-size-adjust: 100%;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            border-collapse: separate;\n" +
                "            mso-table-lspace: 0pt;\n" +
                "            mso-table-rspace: 0pt;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        table td {\n" +
                "            font-family: 'Poppins', 'Helvetica', sans-serif;\n" +
                "            font-size: 15px;\n" +
                "            line-height: 28px;\n" +
                "            letter-spacing: .5px;\n" +
                "            text-align: left;\n" +
                "            color: #6F6F6F;\n" +
                "        }\n" +
                "\n" +
                "        /* -------------------------------------\n" +
                "              BODY & CONTAINER\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        .body {\n" +
                "            background-color: #dbdbdb;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\n" +
                "\n" +
                "        .container {\n" +
                "            display: block;\n" +
                "            Margin: 0 auto !important;\n" +
                "            /* makes it centered */\n" +
                "            max-width: 580px;\n" +
                "            padding: 10px;\n" +
                "            width: 580px;\n" +
                "        }\n" +
                "\n" +
                "        /* This should also be a block element, so that it will fill 100% of the .container */\n" +
                "\n" +
                "        .content {\n" +
                "            box-sizing: border-box;\n" +
                "            display: block;\n" +
                "            Margin: 0 auto;\n" +
                "            max-width: 580px;\n" +
                "            padding: 10px;\n" +
                "        }\n" +
                "\n" +
                "        /* -------------------------------------\n" +
                "              HEADER, FOOTER, MAIN\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        .main {\n" +
                "            background: #ffffff;\n" +
                "            border-radius: 3px;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        .wrapper {\n" +
                "            box-sizing: border-box;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .content-block {\n" +
                "            padding-bottom: 10px;\n" +
                "            padding-top: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            clear: both;\n" +
                "            Margin-top: 10px;\n" +
                "            text-align: center;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        .footer td,\n" +
                "        .footer p,\n" +
                "        .footer span,\n" +
                "        .footer a {\n" +
                "            color: #000000;\n" +
                "            font-size: 14px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        /* -------------------------------------\n" +
                "              TYPOGRAPHY\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        h1 {\n" +
                "            font-family: 'Montserrat', 'Verdana', sans-serif;\n" +
                "            font-size: 30px;\n" +
                "            font-weight: bold;\n" +
                "            line-height: 42px;\n" +
                "            text-align: left;\n" +
                "            color: #000000;\n" +
                "            padding-bottom: 15px !important;\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            font-family: 'Montserrat', 'Verdana', sans-serif;\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            line-height: 32px;\n" +
                "            text-align: left;\n" +
                "            color: #000000;\n" +
                "            padding-bottom: 15px !important;\n" +
                "        }\n" +
                "\n" +
                "        h3 {\n" +
                "            font-family: 'Montserrat', 'Verdana', sans-serif;\n" +
                "            font-size: 20px;\n" +
                "            font-weight: bold;\n" +
                "            line-height: 28px;\n" +
                "            text-align: left;\n" +
                "            color: #000000;\n" +
                "            padding-bottom: 15px !important;\n" +
                "        }\n" +
                "\n" +
                "        p,\n" +
                "        ul,\n" +
                "        ol {\n" +
                "            font-family: 'Poppins', 'Helvetica', sans-serif;\n" +
                "            font-size: 15px;\n" +
                "            font-weight: normal;\n" +
                "            margin: 0;\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        p li,\n" +
                "        ul li,\n" +
                "        ol li {\n" +
                "            list-style-position: inside;\n" +
                "            margin-left: 5px;\n" +
                "        }\n" +
                "\n" +
                "        a {\n" +
                "            color: #39b54a;\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        .btn {\n" +
                "            box-sizing: border-box;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        .btn>tbody>tr>td {\n" +
                "            padding-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .btn table {\n" +
                "            width: auto;\n" +
                "        }\n" +
                "\n" +
                "        .btn table td {\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 5px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .btn a {\n" +
                "            background-color: #ffffff;\n" +
                "            border: solid 1px #39b54a;\n" +
                "            border-radius: 5px;\n" +
                "            box-sizing: border-box;\n" +
                "            color: #39b54a;\n" +
                "            cursor: pointer;\n" +
                "            display: inline-block;\n" +
                "            font-size: 15px;\n" +
                "            font-weight: bold;\n" +
                "            margin: 0;\n" +
                "            padding: 12px 25px;\n" +
                "            text-decoration: none;\n" +
                "            text-transform: capitalize;\n" +
                "        }\n" +
                "\n" +
                "        .btn-primary table td {\n" +
                "            background-color: #39b54a;\n" +
                "        }\n" +
                "\n" +
                "        .btn-primary a {\n" +
                "            background-color: #39b54a;\n" +
                "            border-color: #39b54a;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "\n" +
                "        .preheader {\n" +
                "            color: transparent;\n" +
                "            display: none;\n" +
                "            height: 0;\n" +
                "            max-height: 0;\n" +
                "            max-width: 0;\n" +
                "            opacity: 0;\n" +
                "            overflow: hidden;\n" +
                "            mso-hide: all;\n" +
                "            visibility: hidden;\n" +
                "            width: 0;\n" +
                "        }\n" +
                "\n" +
                "        .powered-by a {\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        hr {\n" +
                "            border: 0;\n" +
                "            border-bottom: 1px solid #dbdbdb;\n" +
                "            Margin: 20px 0;\n" +
                "        }\n" +
                "\n" +
                "        /* -------------------------------------\n" +
                "              RESPONSIVE AND MOBILE FRIENDLY STYLES\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        @media only screen and (max-width: 620px) {\n" +
                "            table[class=body] h1 {\n" +
                "                font-size: 28px !important;\n" +
                "                margin-bottom: 10px !important;\n" +
                "            }\n" +
                "            table[class=body] p,\n" +
                "            table[class=body] ul,\n" +
                "            table[class=body] ol,\n" +
                "            table[class=body] td,\n" +
                "            table[class=body] span,\n" +
                "            table[class=body] a {\n" +
                "                font-size: 16px !important;\n" +
                "            }\n" +
                "            table[class=body] .wrapper,\n" +
                "            table[class=body] .article {\n" +
                "                padding: 10px !important;\n" +
                "            }\n" +
                "            table[class=body] .content {\n" +
                "                padding: 0 !important;\n" +
                "            }\n" +
                "            table[class=body] .container {\n" +
                "                padding: 0 !important;\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "            table[class=body] .main {\n" +
                "                border-left-width: 0 !important;\n" +
                "                border-radius: 0 !important;\n" +
                "                border-right-width: 0 !important;\n" +
                "            }\n" +
                "            table[class=body] .btn table {\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "            table[class=body] .btn a {\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"\">\n" +
                "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">\n" +
                "    <tr>\n" +
                "        <td>&nbsp;</td>\n" +
                "        <td class=\"container\">\n" +
                "            <div class=\"footer\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td class=\"content-block\">\n" +
                "                            <a href=\"https://denvercbdco.com\" target=\"_blank\"><img src=\"https://marketing-image-production.s3.amazonaws.com/uploads/818b5bf43ff7a25df998bce1c0b565f00071afbe201bc77a77005f92dd80426c8bc08e07e78e0fcaed230228a487ac9e3e70293a58da873856d1b38daa504198.png\" alt=\"Denver CBD\" align=\"center\" style=\"display:block;float:none;margin:0 auto;max-width:200px;outline:0;\"></a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "            <div class=\"content\">\n" +
                "\n" +
                "                <span class=\"preheader\">This is preheader text. Some clients will show this text as a preview.</span>\n" +
                "                <table role=\"presentation\" class=\"main\">\n" +
                "\n" +
                "                    <tr>\n" +
                "                        <td class=\"wrapper\">\n" +
                "                            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <h2>Congratulations [name]!</h2>\n" +
                "                                        <p>Your partner application for Denver CBD has been accepted.</p>\n" +
                "                                        <p><b>Partner ID:</b> [partner_id]<br>\n" +
                "                                            <b>Username:</b> [username]</p>\n" +
                "                                        <p>Log in to your <a href=\"https://denvercbdco.com/partner-dashboard/\" target=\"_blank\">Partner Dashboard</a></p>\n" +
                "                                        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\">\n" +
                "                                            <tbody>\n" +
                "                                            <tr>\n" +
                "                                                <td align=\"left\">\n" +
                "                                                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                        <tbody>\n" +
                "                                                        <tr>\n" +
                "                                                            <td> <a href=\"https://denvercbdco.com/partner-dashboard/\" target=\"_blank\"><img src=\"https://denvercbdco.com/wp-content/uploads/2018/11/home-icon.png\" style=\"margin-bottom:-3px;padding-right:10px;width:18px;\"></i>Go to Partner Dashboard</a>                                      </td>\n" +
                "                                                        </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "\n" +
                "                    <!-- END MAIN CONTENT AREA -->\n" +
                "                </table>\n" +
                "\n" +
                "                <!-- START FOOTER -->\n" +
                "                <div class=\"footer\">\n" +
                "                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                        <tr>\n" +
                "                            <td class=\"content-block powered-by\">\n" +
                "                                <a href=\"[unsubscribe]\" target=\"_blank\" style=\"font-weight:bold\">UNSUBSCRIBE</a> &nbsp;\n" +
                "                                <a href=\"https://denvercbdco.com/privacy-policy/\" target=\"_blank\" style=\"font-weight:bold\">PRIVACY POLICY</a> &nbsp;\n" +
                "                                <a href=\"https://denvercbdco.com/partner-agreement/\" target=\"_blank\" style=\"font-weight:bold\">PARTNER AGREEMENT</a> &nbsp;\n" +
                "                                <a href=\"https://denvercbdco.com/partner-dashboard/\" target=\"_blank\" style=\"font-weight:bold\">DASHBOARD LOGIN</a>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"content-block\">\n" +
                "                                <span class=\"apple-link\">This email was sent by the Denver CBD Company,<br>7345 Woodland Dr, Indianapolis, IN 46278</span>\n" +
                "                                <br> Copyright © 2018 Denver CBD Company\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"content-block powered-by\">\n" +
                "                                <a href=\"https://www.facebook.com/DenverCBD\" target=\"_blank\"><img title=\"Facebook\" src=\"https://hoiqh.stripocdn.email/content/assets/img/social-icons/logo-black/facebook-logo-black.png\" alt=\"Facebook\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>                  &nbsp;\n" +
                "                                <a href=\"https://www.instagram.com/denvercbd/\" target=\"_blank\" style=\"font-weight:bold\"><img title=\"Instagram\" src=\"https://hoiqh.stripocdn.email/content/assets/img/social-icons/logo-black/instagram-logo-black.png\" alt=\"Instagram\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>                  &nbsp;\n" +
                "                                <a href=\"https://www.youtube.com/channel/UCVA6ZT2lBB7dH__L3QP-GpA\" target=\"_blank\" style=\"font-weight:bold\"><img title=\"Youtube\" src=\"https://hoiqh.stripocdn.email/content/assets/img/social-icons/logo-black/youtube-logo-black.png\" alt=\"Youtube\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>                  &nbsp;\n" +
                "                                <a href=\"mailto:marketing@denvercbdco.com\" target=\"_blank\" style=\"font-weight:bold\"><img title=\"Email\" src=\"https://hoiqh.stripocdn.email/content/assets/img/other-icons/logo-black/mail-logo-black.png\" alt=\"Email\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "                <!-- END FOOTER -->\n" +
                "\n" +
                "                <!-- END CENTERED WHITE CONTAINER -->\n" +
                "            </div>\n" +
                "        </td>\n" +
                "        <td>&nbsp;</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        sendEmail(to, "A new order has been issued!", text);
        text = "<html><meta charset='UTF-8'>" +
                "<p style='line-height: 3em; font-size: 20px; font-family: Calibri; font-weight: bolder; color: darkblue'>" +
                "We require your attention regarding the following order: <br>" +
                "<table style='width:80%; height: 30%; padding-left: 20%' >" +
                "   <tbody>" +
                "      <tr style='background:azure; color: darkblue; font-weight: bold'>" +
                "          <td style='font-weight: bold; width:25%'>Operation number:</td>" +
                "          <td>" + o.getId() + "</td>" +
                "      </tr>" +
                "      <tr style='background-color: lightskyblue; color: darkblue; font-weight: bold'>" +
                "          <td style='font-weight: bold; width:25%'>Assistant:</td>" +
                "          <td>" + o.getAgent().getName() + " -- " + o.getAgent().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background:azure; color: darkblue; font-weight: bold'>" +
                "          <td style='font-weight: bold; width:20%'>Customer:</td>" +
                "          <td>" + o.getClient().getBusinessName() + " -- " + o.getClient().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background-color: lightskyblue; color: darkblue; font-weight: bold'>" +
                "          <td style='font-weight: bold; width:20%'>Total:</td>" +
                "          <td>" + o.getTotal() + "</td>" +
                "      </tr>" +
                "      <tr style='background: azure; font-weight: bold; color: darkblue;'>" +
                "          <td style='font-weight: bold; width:20%'>Products:</td>" +
                "          <td>" + products + "</td>" +
                "      </tr>" +
                "    </tbody>" +
                "</table>" +
                "</p>" +
                "</html>";
        sendEmail(o.getClient().getEmail(), "Your order has been taken over!", text);

    }

    private void sendEmail(String to, String obj, String text) {

        String test ="pippodima99@gmail.com";
        String from = "ing.software.dimpa@gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("ing.software.dimpa@gmail.com","rkqvtlxwtcaczfjj\n"); //mbvmolrwfpmzcuoq neauczeusvreoesu

            }
        });

        session.setDebug(true);

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(test));
            message.setSubject(obj);
            message.setContent(text,"text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("inviato");

        }catch (MessagingException mex){
            mex.printStackTrace();
        }
    }

}
