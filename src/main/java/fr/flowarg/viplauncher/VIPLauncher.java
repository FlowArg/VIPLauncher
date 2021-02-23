package fr.flowarg.viplauncher;

import fr.flowarg.flowcompat.Platform;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.CurseFileInfos;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.flowarg.viplauncher.auth.microsoft.AuthInfo;
import fr.flowarg.viplauncher.auth.microsoft.MicrosoftAuthenticator;
import fr.flowarg.viplauncher.auth.mojang.Auth;
import fr.flowarg.viplauncher.auth.mojang.exceptions.AuthenticationUnavailableException;
import fr.flowarg.viplauncher.auth.mojang.exceptions.InvalidCredentialsException;
import fr.flowarg.viplauncher.auth.mojang.exceptions.RequestException;
import fr.flowarg.viplauncher.auth.mojang.exceptions.UserMigratedException;
import fr.flowarg.viplauncher.auth.mojang.responses.AuthenticationResponse;
import fr.flowarg.viplauncher.auth.mojang.responses.LoginResponse;
import fr.flowarg.viplauncher.ui.FxApplication;
import fr.flowarg.viplauncher.ui.panels.HomePanel;
import fr.flowarg.viplauncher.utils.Config;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.net.Proxy;
import java.util.UUID;

public class VIPLauncher
{
    private static VIPLauncher instance;
    private final ILogger logger;
    private final File launcherDir = new File(Platform.isOnWindows() ? System.getenv("APPDATA") : Platform.isOnMac() ? System.getProperty("user.home") + "/Library/Application Support/" : System.getProperty("user.home"), ".vip");
    private final Config config;
    private AuthInfos authInfos;

    VIPLauncher() throws Exception
    {
        instance = this;
        this.launcherDir.mkdirs();
        this.logger = new Logger("[VIPLauncher]", new File(this.launcherDir, "launcher.log"));
        this.config = new Config(new File(this.launcherDir, "launcher.data"));
    }

    public void start()
    {
        this.logger.info("Starting launcher.");
        if(this.config.get("ram") == null)
            this.config.set("ram", "1");
        Application.launch(FxApplication.class);
    }

    public boolean auth(String email, String password)
    {
        try
        {
            final AuthenticationResponse response = Auth.authenticate(email, password, UUID.randomUUID().toString(), Proxy.NO_PROXY);

            this.getLogger().info("Authenticated !");
            return this.finalizeAuth(response);
        } catch (RequestException | AuthenticationUnavailableException e)
        {
            this.logger.printStackTrace(e);
            final Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.CLOSE);
            alert.setResizable(true);
            alert.showAndWait();
        }

        return false;
    }

    public boolean refresh() throws UserMigratedException, AuthenticationUnavailableException, InvalidCredentialsException
    {
        final String accessToken, clientToken;
        accessToken = this.config.get("mojang_accessToken");
        clientToken = this.config.get("mojang_clientToken");
        if(accessToken != null && clientToken != null && !accessToken.equalsIgnoreCase("") && !clientToken.equalsIgnoreCase(""))
        {
            final LoginResponse response = Auth.refresh(accessToken, clientToken);

            this.getLogger().info("Authenticated automatically !");
            return this.finalizeAuth(response);
        }
        return false;
    }

    private boolean finalizeAuth(LoginResponse response)
    {
        final String repClientToken = response.getClientToken();
        final String repAccessToken = response.getAccessToken();
        final String username = response.getSelectedProfile().getName();
        final String uuid = response.getSelectedProfile().getUUID().toString();

        this.config.set("mojang_clientToken", repClientToken);
        this.config.set("mojang_accessToken", repAccessToken);
        this.config.set("mojang_username", username);
        this.config.set("mojang_uuid", uuid);
        this.authInfos = new AuthInfos(username, repAccessToken, uuid);
        return true;
    }

    public void authWithMicrosoft(MicrosoftAuthenticator microsoftAuthenticator)
    {
        final AuthInfo authInfo = microsoftAuthenticator.auth();
        this.config.set("microsoft_accessToken", authInfo.getToken());
        this.config.set("microsoft_username", authInfo.getUsername());
        this.config.set("microsoft_uuid", authInfo.getUUID().toString());
        this.authInfos = new AuthInfos(authInfo.getUsername(), authInfo.getToken(), authInfo.getUUID().toString());
    }

    public void update() throws Exception
    {
        final IProgressCallback callback = HomePanel.getHomePanel();
        final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
                .withEnableCurseForgePlugin(true)
                .build();
        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
                .withName("1.15.2")
                .withVersionType(VersionType.FORGE)
                .build();
        final AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
                .withFileDeleter(new ModFileDeleter(true))
                .withForgeVersion("31.2.45")
                .withVanillaVersion(version)
                .withLogger(this.logger)
                .withNoGui(true)
                .withProgressCallback(callback)
                .withCurseMods(CurseFileInfos.getFilesFromJson("https://flowarg.github.io/minecraft/launcher/vip/cursemods.json"))
                .withMods(Mod.getModsFromJson("https://flowarg.github.io/minecraft/launcher/vip/mods.json"))
                .build();
        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                .withVersion(version)
                .withProgressCallback(callback)
                .withUpdaterOptions(options)
                .withLogger(this.logger)
                .withForgeVersion(forgeVersion)
                .build();
        updater.update(this.launcherDir);
    }

    public void launch() throws Exception
    {
        final GameVersion version = new GameVersion("1.15.2", GameType.V1_13_HIGHER_FORGE.setNewForgeVersionDiscriminator(new NewForgeVersionDiscriminator("31.2.45", "1.15.2", "net.minecraftforge", "20200515.085601")));
        final GameInfos infos = new GameInfos("vip", this.launcherDir, version, new GameTweak[0]);
        final ExternalLauncher launcher = new ExternalLauncher(MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, this.authInfos));
        launcher.getProfile().getVmArgs().add("-Xmx" + Math.round(Double.parseDouble(this.getConfig().get("ram"))) + "G");
        launcher.getProfile().getVmArgs().add("-Xms256M");
        launcher.launch();
        Thread.sleep(2000L);
        System.exit(0);
    }

    public ILogger getLogger()
    {
        return this.logger;
    }

    public Config getConfig()
    {
        return this.config;
    }

    public static VIPLauncher getInstance()
    {
        return instance;
    }
}
