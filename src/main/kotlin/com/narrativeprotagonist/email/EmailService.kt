package com.narrativeprotagonist.email

import com.narrativeprotagonist._global.config.EmailProperties
import com.narrativeprotagonist.auth.domain.LoginToken
import jakarta.mail.internet.MimeMessage
import org.springframework.context.MessageSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val emailProperties: EmailProperties,
    private val messageSource: MessageSource
) {
    /**
     * 이메일 인증 메일 발송
     */
    fun sendVerificationEmail(email: String, locale: Locale = Locale.KOREAN): Long {
        val expiredAt = System.currentTimeMillis() + 15 * 60 * 1000 // 15분

        val context = Context(locale).apply {
            setVariable("verificationLink", "${emailProperties
                .baseUrl}/api/auth/verify?email=$email&expiredAt=$expiredAt")
        }

        val subject = messageSource.getMessage("email.verification.subject", null, locale)
        val content = templateEngine.process("email/verification", context)

        sendHtmlEmail(email, subject, content)
        return expiredAt
    }

    /**
     * 로그인 매직 링크 메일 발송
     */
    fun sendSignInEmail(to: String, locale: Locale = Locale.KOREAN, loginToken: String) {
        val loginUrl = "${emailProperties.baseUrl}/api/auth/verify-login?token=$loginToken"

        val context = Context(locale).apply {
            setVariable("loginUrl", loginUrl)
        }

        val subject = messageSource.getMessage("email.login.subject", null, locale)
        val content = templateEngine.process("email/login", context)

        sendHtmlEmail(to, subject, content)
    }

    /**
     * 회원가입 환영 메일 발송
     */
    fun sendWelcomeEmail(to: String, locale: Locale = Locale.KOREAN) {
        val context = Context(locale)

        val subject = messageSource.getMessage("email.welcome.subject", null, locale)
        val content = templateEngine.process("email/welcome", context)

        sendHtmlEmail(to, subject, content)
    }

    private fun sendHtmlEmail(to: String, subject: String, content: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(emailProperties.from)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, true) // true = HTML

        mailSender.send(message)
    }
}
