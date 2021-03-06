package io.mosip.registration.controller;

import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_ID;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_NAME;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.registration.config.AppConfig;
import io.mosip.registration.constants.RegistrationConstants;
import io.mosip.registration.context.SessionContext;
import io.mosip.registration.controller.auth.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class for initializing the application
 * 
 * @author Sravya Surampalli
 * @since 1.0.0
 *
 */
@Component
public class Initialization extends Application {

	/**
	 * Instance of {@link Logger}
	 */
	private static final Logger LOGGER = AppConfig.getLogger(Initialization.class);

	private static ApplicationContext applicationContext;
	private static Stage applicationPrimaryStage;
	private static String upgradeServer = null;
	private static String tpmRequired = "Y";
	private static String applicationStartTime;

	@Override
	public void start(Stage primaryStage) {
		try {
			LOGGER.info("REGISTRATION - LOGIN SCREEN INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID, "Login screen initilization "
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis()));

			io.mosip.registration.context.ApplicationContext.setUpgradeServerURL(upgradeServer);
			io.mosip.registration.context.ApplicationContext.setTPMUsageFlag(tpmRequired);

			setPrimaryStage(primaryStage);
			LoginController loginController = applicationContext.getBean(LoginController.class);
			loginController.loadInitialScreen(primaryStage);
			loginController.loadUIElementsFromSchema();
			SessionContext.setApplicationContext(applicationContext);

			LOGGER.info("REGISTRATION - LOGIN SCREEN INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID, "Login screen loaded"
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis()));
		} catch (Exception exception) {
			LOGGER.error("REGISTRATION - APPLICATION INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID,
					"Application Initilization Error"
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis())
							+ ExceptionUtils.getStackTrace(exception));
		}
	}

	public static void main(String[] args) {
		try {
			System.setProperty("java.net.useSystemProxies", "true");
			System.setProperty("file.encoding", "UTF-8");
			
			io.mosip.registration.context.ApplicationContext.getInstance();
			if (args.length > 1) {
				upgradeServer = args[0];
				tpmRequired = args[1];
				io.mosip.registration.context.ApplicationContext.setTPMUsageFlag(args[1]);
			}
			Timestamp time = Timestamp.valueOf(DateUtils.getUTCCurrentDateTime());
			applicationStartTime = String.valueOf(time);

			applicationContext = createApplicationContext();
			launch(args);

			LOGGER.info("REGISTRATION - APPLICATION INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID, "Application Initilization"
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis()));
		} catch (Exception exception) {
			LOGGER.error("REGISTRATION - APPLICATION INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID,
					"Application Initilization Error"
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis())
							+ ExceptionUtils.getStackTrace(exception));
		}
	}

	/**
	 * Create Application context with AppConfig Class
	 * @return Spring Application context 
	 */
	public static ApplicationContext createApplicationContext() {

		if(System.getProperty(RegistrationConstants.MOSIP_HOSTNAME)==null && System.getenv(RegistrationConstants.MOSIP_HOSTNAME)!=null) {
			
			System.setProperty(RegistrationConstants.MOSIP_HOSTNAME, System.getenv(RegistrationConstants.MOSIP_HOSTNAME));
		}
		
		return new AnnotationConfigApplicationContext(AppConfig.class);
	}

	@Override
	public void stop() {
		try {
			super.stop();
			getClientCryptoFacade().getClientSecurity().closeSecurityInstance();
			LOGGER.info("REGISTRATION - APPLICATION INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID, "Closed the Client Security Instance");
		} catch (Exception exception) {
			LOGGER.error("REGISTRATION - APPLICATION INITILIZATION - REGISTRATIONAPPINITILIZATION", APPLICATION_NAME,
					APPLICATION_ID,
					"Application Initilization Error"
							+ new SimpleDateFormat(RegistrationConstants.HH_MM_SS).format(System.currentTimeMillis())
							+ ExceptionUtils.getStackTrace(exception));
		} finally {
			System.exit(0);
		}
	}

	private ClientCryptoFacade getClientCryptoFacade() {
		return applicationContext.getBean(ClientCryptoFacade.class);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		Initialization.applicationContext = applicationContext;
	}

	public static Stage getPrimaryStage() {
		return applicationPrimaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		applicationPrimaryStage = primaryStage;
	}
	
	public static String getApplicationStartTime() {
		return applicationStartTime;
	}
}
