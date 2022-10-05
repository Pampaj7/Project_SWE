package operationsManager;

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
                "            background-color: lightcyan;\n" +
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
                "            color: black;\n" +
                "        }\n" +
                "\n" +
                "        /* -------------------------------------\n" +
                "              BODY & CONTAINER\n" +
                "          ------------------------------------- */\n" +
                "\n" +
                "        .body {\n" +
                "            background-color: white;\n" +
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
                "            background: lightskyblue;\n" +
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
                "            font-size: 18px;\n" +
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
                "                        <td>\n" +
                "                  <img height=\"25%\" width=\"25%\" src=\"https://scontent-mxp1-1.xx.fbcdn.net/v/t39.30808-6/248305243_540750100658412_7238838522423109523_n.png?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=hqULVtUXGqIAX8NNNXb&_nc_ht=scontent-mxp1-1.xx&oh=00_AT-oKHmUoe76Cfb86O6TNyUtsMdUe9B2LxBq-xV1LkMM3Q&oe=6339869B\">\n" +
                "                </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "            <div class=\"content\">\n" +
                "\n" +
                "                <span class=\"preheader\">Ciao! Ecco il riepilogo della tua operazione appena svolta!</span>\n" +
                "                <table role=\"presentation\" class=\"main\">\n" +
                "\n" +
                "                    <tr>\n" +
                "                        <td class=\"wrapper\">\n" +
                "                            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <h2>Grazie " + o.getCustomer().getBusinessName() + "!</h2>\n" +
                "                                        <p  style=\"font-size: 20px\">Ecco il riepilogo del tuo intervento.</p>\n" +
                "                                        <p><b>Assistente:</b>" + o.getAssistant().getName() + "<br>\n" +
                "                                            <b>Operazione Numero:</b> " + o.getId() + " </p>\n" +
                "                                            <b>Cliente:</b> " + o.getCustomer().getBusinessName() + "<br>\n" +
                "                                            <b>Prodotti usati:</b> <br><p style=\"padding-left: 5%\">" + products + "</p>\n" +
                "                                            <b>Costo totale:</b> " + o.getTotal() +
                "                                        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\">\n" +
                "                                            <tbody>\n" +
                "                                            <tr>\n" +
                "                                                <td align=\"left\">\n" +
                "                                                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                        <tbody>\n" +
                "                                                        <tr>\n" +
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
                "                               </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"content-block\">\n" +
                "                                <span class=\"apple-link\">Questa email è stata inviata automaticamente da: Studio Dentistico Di Martino Nicola " +
                "                                <br>Via Michelangelo Buonarroti, 15 51031 Agliana, Toscana</span>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"content-block powered-by\">\n" +
                "                                <a href=\"https://www.facebook.com/studiodentistico.dimartinonicola\" target=\"_blank\"><img title=\"Facebook\" src=\"https://hoiqh.stripocdn.email/content/assets/img/social-icons/logo-black/facebook-logo-black.png\" alt=\"Facebook\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>                  &nbsp;\n" +
                "                                <a href=\"https://www.instagram.com/studio_dentistico_di_martino/\" target=\"_blank\" style=\"font-weight:bold\"><img title=\"Instagram\" src=\"https://hoiqh.stripocdn.email/content/assets/img/social-icons/logo-black/instagram-logo-black.png\" alt=\"Instagram\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>                  &nbsp;\n" +
                "                                <a href=\"mailto:ing.software.dimpa@gmail.com\" target=\"_blank\" style=\"font-weight:bold\"><img title=\"Email\" src=\"https://hoiqh.stripocdn.email/content/assets/img/other-icons/logo-black/mail-logo-black.png\" alt=\"Email\" width=\"32\" height=\"32\" style=\"display:inline-block;border:0;outline:none;text-decoration:none;\"></a>\n" +
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

        //*******************************************************************************+
        sendEmail(o.getAssistant().getEmail(), "Riepilogo del tuo intervento", text);

        text = "<table bgcolor=\"#8DCBEF\" width=\"100%\" border=\"0\" cellpadding=\"30\" cellspacing=\"0\">\n" +
                "  <tr>\n" +
                "    <td>\n" +
                "      <table width=\"630\"  bgcolor=\"#ffffff\" cellpadding=\"30\"  align=\"center\" >\n" +
                "        <tr>\n" +
                "          <td>\n" +
                "            <table bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"line-height:22px;\">\n" +
                "              <tr>\n" +
                "                <td>\n" +
                "                  <img height=\"25%\" width=\"25%\" src=\"https://scontent-mxp1-1.xx.fbcdn.net/v/t39.30808-6/248305243_540750100658412_7238838522423109523_n.png?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=hqULVtUXGqIAX8NNNXb&_nc_ht=scontent-mxp1-1.xx&oh=00_AT-oKHmUoe76Cfb86O6TNyUtsMdUe9B2LxBq-xV1LkMM3Q&oe=6339869B\">\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td height=\"45\"></td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td color=\"red\">\n" +
                "                  <font color=\"#1E285B\" face=\"arial\" size=\"5\">  <b> Nuova operazione effettuata </b>  </font>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td height=\"30\"></td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td>\n" +
                "                  <font color=\"#1E285B\" face=\"arial\" size=\"3\"><b>Assistente:</b> " + o.getAssistant().getName() + "</font>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td> <font color=\"#1E285B\" face=\"arial\" size=\"3\"><b>Cliente:</b> " + o.getCustomer().getBusinessName() + "</font> </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td> <font color=\"#1E285B\" face=\"arial\" size=\"3\"><b>Prodotti utilizzati:</b><p style=\"padding-left: 5%\">" + products + "</font> </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td> <font color=\"#1E285B\" face=\"arial\" size=\"3\"><b>Totale:</b> " + o.getTotal() + " euro </font> </td>\n" +
                "              </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>";
        sendEmail(to, "Nuovo intervento effettuato", text);

    }

    private void sendEmail(String to, String obj, String text) {

        String test = "pippodima99@gmail.com";
        String from = "ing.software.dimpa@gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ing.software.dimpa@gmail.com", "rkqvtlxwtcaczfjj\n"); //mbvmolrwfpmzcuoq neauczeusvreoesu

            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(obj);
            message.setContent(text, "text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("inviato");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
