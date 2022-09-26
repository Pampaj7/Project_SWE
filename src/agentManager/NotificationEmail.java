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
        text = "<html><meta charset='UTF-8'>" +
                "<p style='line-height: 2em; font-size: 16px; font-family: Calibri;'>" +
                "We require your attention regarding the following order: <br>" +
                "<table style='width:100%;'>" +
                "   <tbody>" +
                "      <tr style='background:#f5f2f2'>" +
                "          <td style='font-weight: bold; width:20%'>Operation number:</td>" +
                "          <td>" + o.getId() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#e1e1e1'>" +
                "          <td style='font-weight: bold; width:20%'>Assistant:</td>" +
                "          <td>" + o.getAgent().getName() + " -- " + o.getAgent().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#f5f2f2'>" +
                "          <td style='font-weight: bold; width:20%'>Customer:</td>" +
                "          <td>" + o.getClient().getBusinessName() + " -- " + o.getClient().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#e1e1e1'>" +
                "          <td style='font-weight: bold; width:20%'>Total:</td>" +
                "          <td>" + o.getTotal() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#f5f2f2'>" +
                "          <td style='font-weight: bold; width:20%'>Commission:</td>" +
                "          <td>" + o.getCommissionTot() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#e1e1e1'>" +
                "          <td style='font-weight: bold; width:20%'>Products:</td>" +
                "          <td>" + products + "</td>" +
                "      </tr>" +
                "    </tbody>" +
                "</table>" +
                "</p>" +
                "</html>";
        sendEmail(to, "A new order has been issued!", text);
        text = "<html><meta charset='UTF-8'>" +
                "<p style='line-height: 2em; font-size: 16px; font-family: Calibri;'>" +
                "We require your attention regarding the following order: <br>" +
                "<table style='width:100%;'>" +
                "   <tbody>" +
                "      <tr style='background:#f5f2f2'>" +
                "          <td style='font-weight: bold; width:20%'>Operation number:</td>" +
                "          <td>" + o.getId() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#e1e1e1'>" +
                "          <td style='font-weight: bold; width:20%'>Assistant:</td>" +
                "          <td>" + o.getAgent().getName() + " -- " + o.getAgent().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#f5f2f2'>" +
                "          <td style='font-weight: bold; width:20%'>Customer:</td>" +
                "          <td>" + o.getClient().getBusinessName() + " -- " + o.getClient().getEmail() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#e1e1e1'>" +
                "          <td style='font-weight: bold; width:20%'>Total:</td>" +
                "          <td>" + o.getTotal() + "</td>" +
                "      </tr>" +
                "      <tr style='background:#f5f2f2'>" +
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
                return new PasswordAuthentication("ing.software.dimpa@gmail.com","neauczeusvreoesu\n"); //mbvmolrwfpmzcuoq neauczeusvreoesu

            }
        });

        session.setDebug(true);

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
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
