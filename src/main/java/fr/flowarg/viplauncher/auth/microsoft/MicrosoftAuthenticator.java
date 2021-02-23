package fr.flowarg.viplauncher.auth.microsoft;

// Adapted from MiniLauncher (a project of MiniDigger), see his amazing work on GitHub !
@FunctionalInterface
public interface MicrosoftAuthenticator
{
    AuthInfo auth();
}
