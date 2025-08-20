package health.care.medicore.Utils;

public class Constants {

    public static final String OWNER = "OWNER";

    public static final String ADMIN = "ADMIN";

    public static final String DOCTOR = "DOCTOR";

    public static final String NURSE = "NURSE";

    public static final String RECEPTION = "RECEPTION";

    public static final String PATIENT = "PATIENT";

    public static final String CHAT_USER_ROLE = "user";

    public static final String CHAT_ASSISTANT_ROLE = "assistant";

    public static final String EMAIL_OTP_TEMPLATE_NAME = "EMAIL_OTP_TEMPLATE_NAME";

    public static final String FORGOT_PASSWORD_OTP_TEMPLATE_NAME = "FORGOT_PASSWORD_OTP_TEMPLATE_NAME";

    public static final String APPOINTMENT_CONFIRM_TEMPLATE_NAME = "APPOINTMENT_CONFIRM_TEMPLATE_NAME";

    public static final String APPOINTMENT_CANCEL_TEMPLATE_NAME = "APPOINTMENT_CANCEL_TEMPLATE_NAME";

    public static final String APPOINTMENT_CONFIRM_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>OTP Verification - Medicore</title>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "  <style>\n" +
            "    body {\n" +
            "      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
            "      background-color: #f4f9ff;\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "      color: #333;\n" +
            "    }\n" +
            "    .email-container {\n" +
            "      max-width: 600px;\n" +
            "      margin: 40px auto;\n" +
            "      background-color: #ffffff;\n" +
            "      border-radius: 10px;\n" +
            "      overflow: hidden;\n" +
            "      box-shadow: 0 0 20px rgba(0,0,0,0.05);\n" +
            "    }\n" +
            "    .email-header {\n" +
            "      background-color: #0066cc;\n" +
            "      padding: 20px;\n" +
            "      text-align: center;\n" +
            "      color: white;\n" +
            "    }\n" +
            "    .email-header img {\n" +
            "      max-width: 150px;\n" +
            "      margin-bottom: 10px;\n" +
            "    }\n" +
            "    .email-body {\n" +
            "      padding: 30px;\n" +
            "    }\n" +
            "    .greeting {\n" +
            "      font-size: 18px;\n" +
            "      margin-bottom: 20px;\n" +
            "    }\n" +
            "    .otp-box {\n" +
            "      background-color: #eaf6ff;\n" +
            "      padding: 15px;\n" +
            "      text-align: center;\n" +
            "      border-radius: 6px;\n" +
            "      font-size: 28px;\n" +
            "      font-weight: bold;\n" +
            "      letter-spacing: 6px;\n" +
            "      color: #0077cc;\n" +
            "      margin: 20px 0;\n" +
            "    }\n" +
            "    .note {\n" +
            "      font-size: 14px;\n" +
            "      color: #666;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .email-footer {\n" +
            "      text-align: center;\n" +
            "      font-size: 13px;\n" +
            "      color: #aaa;\n" +
            "      padding: 20px;\n" +
            "    }\n" +
            "    @media only screen and (max-width: 600px) {\n" +
            "      .email-body {\n" +
            "        padding: 20px;\n" +
            "      }\n" +
            "      .otp-box {\n" +
            "        font-size: 24px;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div class=\"email-container\">\n" +
            "    <div class=\"email-header\">\n" +
            "      <img src=\"https://res.cloudinary.com/dyrbo1wml/image/upload/v1750841014/Logo_ofqiag.png\" alt=\"Company Logo\">\n" +
            "      <h2>Appointment Confirmation</h2>\n" +
            "    </div>\n" +
            "    <div class=\"email-body\">\n" +
            "      <p class=\"greeting\">Hi <strong>[[name]]</strong>,</p>\n" +
            "      <p>Appointment has been confirmed and scheduled between [[doctor]] and [[patient]] on Date/Time: [[dateTime]]\n" +
            "      <p class=\"note\">If you did not request this, please ignore this email or contact our support team immediately.</p>\n" +
            "    </div>\n" +
            "    <div class=\"email-footer\">\n" +
            "      &copy; 2025 Medicore Health Systems. All rights reserved.\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>\n";

    public static final String APPOINTMENT_CANCEL_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>OTP Verification - Medicore</title>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "  <style>\n" +
            "    body {\n" +
            "      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
            "      background-color: #f4f9ff;\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "      color: #333;\n" +
            "    }\n" +
            "    .email-container {\n" +
            "      max-width: 600px;\n" +
            "      margin: 40px auto;\n" +
            "      background-color: #ffffff;\n" +
            "      border-radius: 10px;\n" +
            "      overflow: hidden;\n" +
            "      box-shadow: 0 0 20px rgba(0,0,0,0.05);\n" +
            "    }\n" +
            "    .email-header {\n" +
            "      background-color: #0066cc;\n" +
            "      padding: 20px;\n" +
            "      text-align: center;\n" +
            "      color: white;\n" +
            "    }\n" +
            "    .email-header img {\n" +
            "      max-width: 150px;\n" +
            "      margin-bottom: 10px;\n" +
            "    }\n" +
            "    .email-body {\n" +
            "      padding: 30px;\n" +
            "    }\n" +
            "    .greeting {\n" +
            "      font-size: 18px;\n" +
            "      margin-bottom: 20px;\n" +
            "    }\n" +
            "    .otp-box {\n" +
            "      background-color: #eaf6ff;\n" +
            "      padding: 15px;\n" +
            "      text-align: center;\n" +
            "      border-radius: 6px;\n" +
            "      font-size: 28px;\n" +
            "      font-weight: bold;\n" +
            "      letter-spacing: 6px;\n" +
            "      color: #0077cc;\n" +
            "      margin: 20px 0;\n" +
            "    }\n" +
            "    .note {\n" +
            "      font-size: 14px;\n" +
            "      color: #666;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .email-footer {\n" +
            "      text-align: center;\n" +
            "      font-size: 13px;\n" +
            "      color: #aaa;\n" +
            "      padding: 20px;\n" +
            "    }\n" +
            "    @media only screen and (max-width: 600px) {\n" +
            "      .email-body {\n" +
            "        padding: 20px;\n" +
            "      }\n" +
            "      .otp-box {\n" +
            "        font-size: 24px;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div class=\"email-container\">\n" +
            "    <div class=\"email-header\">\n" +
            "      <img src=\"https://res.cloudinary.com/dyrbo1wml/image/upload/v1750841014/Logo_ofqiag.png\" alt=\"Company Logo\">\n" +
            "      <h2>Appointment Cancellation</h2>\n" +
            "    </div>\n" +
            "    <div class=\"email-body\">\n" +
            "      <p class=\"greeting\">Hi <strong>[[name]]</strong>,</p>\n" +
            "      <p>Appointment has been cancelled between [[doctor]] and [[patient]] on Date/Time: [[dateTime]]\n" +
            "      <p class=\"note\">If you did not request this, please ignore this email or contact our support team immediately.</p>\n" +
            "    </div>\n" +
            "    <div class=\"email-footer\">\n" +
            "      &copy; 2025 Medicore Health Systems. All rights reserved.\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>\n";

    public static final String EMAIL_OTP_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>OTP Verification - Medicore</title>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "  <style>\n" +
            "    body {\n" +
            "      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
            "      background-color: #f4f9ff;\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "      color: #333;\n" +
            "    }\n" +
            "    .email-container {\n" +
            "      max-width: 600px;\n" +
            "      margin: 40px auto;\n" +
            "      background-color: #ffffff;\n" +
            "      border-radius: 10px;\n" +
            "      overflow: hidden;\n" +
            "      box-shadow: 0 0 20px rgba(0,0,0,0.05);\n" +
            "    }\n" +
            "    .email-header {\n" +
            "      background-color: #0066cc;\n" +
            "      padding: 20px;\n" +
            "      text-align: center;\n" +
            "      color: white;\n" +
            "    }\n" +
            "    .email-header img {\n" +
            "      max-width: 150px;\n" +
            "      margin-bottom: 10px;\n" +
            "    }\n" +
            "    .email-body {\n" +
            "      padding: 30px;\n" +
            "    }\n" +
            "    .greeting {\n" +
            "      font-size: 18px;\n" +
            "      margin-bottom: 20px;\n" +
            "    }\n" +
            "    .otp-box {\n" +
            "      background-color: #eaf6ff;\n" +
            "      padding: 15px;\n" +
            "      text-align: center;\n" +
            "      border-radius: 6px;\n" +
            "      font-size: 28px;\n" +
            "      font-weight: bold;\n" +
            "      letter-spacing: 6px;\n" +
            "      color: #0077cc;\n" +
            "      margin: 20px 0;\n" +
            "    }\n" +
            "    .note {\n" +
            "      font-size: 14px;\n" +
            "      color: #666;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .email-footer {\n" +
            "      text-align: center;\n" +
            "      font-size: 13px;\n" +
            "      color: #aaa;\n" +
            "      padding: 20px;\n" +
            "    }\n" +
            "    @media only screen and (max-width: 600px) {\n" +
            "      .email-body {\n" +
            "        padding: 20px;\n" +
            "      }\n" +
            "      .otp-box {\n" +
            "        font-size: 24px;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div class=\"email-container\">\n" +
            "    <div class=\"email-header\">\n" +
            "      <img src=\"https://res.cloudinary.com/dyrbo1wml/image/upload/v1750841014/Logo_ofqiag.png\" alt=\"Company Logo\">\n" +
            "      <h2>OTP Verification</h2>\n" +
            "    </div>\n" +
            "    <div class=\"email-body\">\n" +
            "      <p class=\"greeting\">Hi <strong>[[name]]</strong>,</p>\n" +
            "      <p>To complete your verification, please use the following One-Time Password (OTP):</p>\n" +
            "      <div class=\"otp-box\">[[code]]</div>\n" +
            "      <p class=\"note\">If you did not request this, please ignore this email or contact our support team immediately.</p>\n" +
            "    </div>\n" +
            "    <div class=\"email-footer\">\n" +
            "      &copy; 2025 Medicore Health Systems. All rights reserved.\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>\n";

    public static final String FORGOT_PASSWORD_OTP_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>OTP Verification - Medicore</title>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "  <style>\n" +
            "    body {\n" +
            "      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\n" +
            "      background-color: #f4f9ff;\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "      color: #333;\n" +
            "    }\n" +
            "    .email-container {\n" +
            "      max-width: 600px;\n" +
            "      margin: 40px auto;\n" +
            "      background-color: #ffffff;\n" +
            "      border-radius: 10px;\n" +
            "      overflow: hidden;\n" +
            "      box-shadow: 0 0 20px rgba(0,0,0,0.05);\n" +
            "    }\n" +
            "    .email-header {\n" +
            "      background-color: #0066cc;\n" +
            "      padding: 20px;\n" +
            "      text-align: center;\n" +
            "      color: white;\n" +
            "    }\n" +
            "    .email-header img {\n" +
            "      max-width: 150px;\n" +
            "      margin-bottom: 10px;\n" +
            "    }\n" +
            "    .email-body {\n" +
            "      padding: 30px;\n" +
            "    }\n" +
            "    .greeting {\n" +
            "      font-size: 18px;\n" +
            "      margin-bottom: 20px;\n" +
            "    }\n" +
            "    .otp-box {\n" +
            "      background-color: #eaf6ff;\n" +
            "      padding: 15px;\n" +
            "      text-align: center;\n" +
            "      border-radius: 6px;\n" +
            "      font-size: 28px;\n" +
            "      font-weight: bold;\n" +
            "      letter-spacing: 6px;\n" +
            "      color: #0077cc;\n" +
            "      margin: 20px 0;\n" +
            "    }\n" +
            "    .note {\n" +
            "      font-size: 14px;\n" +
            "      color: #666;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .email-footer {\n" +
            "      text-align: center;\n" +
            "      font-size: 13px;\n" +
            "      color: #aaa;\n" +
            "      padding: 20px;\n" +
            "    }\n" +
            "    @media only screen and (max-width: 600px) {\n" +
            "      .email-body {\n" +
            "        padding: 20px;\n" +
            "      }\n" +
            "      .otp-box {\n" +
            "        font-size: 24px;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div class=\"email-container\">\n" +
            "    <div class=\"email-header\">\n" +
            "      <img src=\"https://res.cloudinary.com/dyrbo1wml/image/upload/v1750841014/Logo_ofqiag.png\" alt=\"Company Logo\">\n" +
            "      <h2>OTP Verification</h2>\n" +
            "    </div>\n" +
            "    <div class=\"email-body\">\n" +
            "      <p class=\"greeting\">Hi <strong>[[name]]</strong>,</p>\n" +
            "      <p>To reset your password, please use the following One-Time Password (OTP):</p>\n" +
            "      <div class=\"otp-box\">[[code]]</div>\n" +
            "      <p class=\"note\">If you did not request this, please ignore this email or contact our support team immediately.</p>\n" +
            "    </div>\n" +
            "    <div class=\"email-footer\">\n" +
            "      &copy; 2025 Medicore Health Systems. All rights reserved.\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>\n";
}
