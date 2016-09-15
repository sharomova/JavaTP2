package partie.pkg1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TP2 Partie 1
 *
 * Programme d'extraction des information sur les articles
 *
 * Date: 9 avril 2014
 *
 * @author Olga Sharomova
 */
public class Partie1 {

    private static class Journaliste {

        int identifiant;//identifiant du journaliste
        String nom;//nom du journaliste
        String prenom;//prenom du journaliste
        String courriel;//couriele du journaliste
        String telephone;//telephone du journaliste
        String cod;//cod d'utilisateur
        Article[] articles;//tablaue sur les articles
    }

    private static class Article {

        int identifiant;//identifiant d'article
        String adresseXML;//adresse du fichier XML
        String section;//section d'article
        String categorie;//categorie d'article
        String nomFichier;//nom du fichier
        Date dateCreation;//date creation d'article
        String titre;//titre d'article
        String motsCles;//les mots-clés
    }

    private static class Date {

        int jour;//jour creation
        int mois;//mois creation
        int annee;//année creation
    }
    /**
     * Le nom du fichier txt à lire
     */
    private static final String NOM_FICHIER_TXT = "journalistes.txt";
    /**
     * Le nom du fichier binaire à écrire
     */
    private static final String NOM_FICHIER_DATA = "journalistes.data";

    public static void main(String[] args) throws FileNotFoundException,
            IOException {
        // déclaration et initialisation à “rien”
        BufferedReader lecteurFichier = null;
        // déclaration et initialisation à “rien”
        DataOutputStream fichierBinaire = null;
        Journaliste desJournaliste = new Journaliste();
        Article unArticle = new Article();
        Date uneDate = new Date();
        int nbJourn = 0;// nombre journalistes
        int nbArticle = 0;// nombre article
        desJournaliste.nom = "";//nom du journaliste
        desJournaliste.prenom = "";// prenom du journaliste
        desJournaliste.courriel = "";// couriele du journaliste
        desJournaliste.telephone = "";// telephone du journaliste
        desJournaliste.cod = "";//code d'utilisateur

        ouvrirFichierTxt(lecteurFichier);
        extraireDonnes(lecteurFichier, fichierBinaire, unArticle,
                desJournaliste, uneDate, nbJourn, nbArticle);
    }

    /**
     * Méthode qui ouvre le fichier txt
     *
     * @throws IOException
     */
    private static void ouvrirFichierTxt(BufferedReader lecteurFichier)
            throws FileNotFoundException, IOException {
        String ligne = "";
        try {
            // Ouverture du fichier
            lecteurFichier = new BufferedReader(new FileReader(NOM_FICHIER_TXT));
        } catch (IOException e) {
            System.out.println("Erreur d'ouverture du fichier,"
                    + " arrêt du programme");
        }
        while ((ligne = lecteurFichier.readLine()) != null) {
        }
    }

    /**
     * Méthode qui lit l'information de journalistes sur fichier txt
     *
     * @param unArticle
     * @param desJournaliste
     * @param uneDatele
     * @param nbJourn
     * @param nbArticle
     * @throws IOException
     */
    private static void extraireDonnes(BufferedReader lecteurFichier,
            DataOutputStream fichierBinaire, Article unArticle,
            Journaliste desJournaliste, Date uneDate, int nbJourn,
            int nbArticle) throws FileNotFoundException, IOException {
        int nb = 0;//compteur de journalistes
        lecteurFichier = new BufferedReader(new FileReader(NOM_FICHIER_TXT));
        try {
            // Ouverture du fichier
            fichierBinaire = new DataOutputStream(
                    new FileOutputStream(NOM_FICHIER_DATA));
        } catch (IOException e) {
            System.out.println("Erreur d'ouverture du fichier,"
                    + " arrêt du programme");
        }
        nbJourn = Integer.parseInt(lecteurFichier.readLine());
        fichierBinaire.writeInt(nbJourn);
        do {
            desJournaliste.identifiant =
                    Integer.parseInt(lecteurFichier.readLine());
            nbArticle = Integer.parseInt(lecteurFichier.readLine());
//ecrire informatione sur les journalistes
            ecrireJournaliste(fichierBinaire, desJournaliste, nbArticle);

            for (int i = 0; i < nbArticle; i++) {
                unArticle.adresseXML = lecteurFichier.readLine();
//prendre information sur cite radio-canada
                fuchierUrl(unArticle);

                Pattern patron2 = Pattern.compile("([a-z]*://)([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)(/)*([A-Za-z-0-9]*)/"
                        + "([0-9]*)(/)*([0-9]*)(/)*([0-9]*)(/)*"
                        + "([0-9A-Za-z-.]*)");
                Matcher verif2 = patron2.matcher(unArticle.adresseXML);
                if (verif2.matches()) {
                    unArticle.section = verif2.group(3);
                }
                Pattern patron3 = Pattern.compile("([a-z]*://)([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)/([A-Za-z-0-9]*)/"
                        + "([0-9]*)/([0-9]*)/([0-9]*)/"
                        + "([0-9A-Za-z-.]*)");
                Matcher verif3 = patron3.matcher(unArticle.adresseXML);
                if (verif3.matches()) {
                    unArticle.categorie = verif3.group(4);
                }
                Pattern patronFichier = Pattern.compile("([a-z]*://)"
                        + "([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)/([A-Za-z-0-9]*)/"
                        + "([0-9]*)/([0-9]*)/([0-9]*)/"
                        + "([0-9A-Za-z-.]*)");
                Matcher verifFichier =
                        patronFichier.matcher(unArticle.adresseXML);
                if (verifFichier.matches()) {
                    unArticle.nomFichier = verifFichier.group(8);
                }
                Pattern patronJoure = Pattern.compile("([a-z]*://)"
                        + "([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)/([A-Za-z-0-9]*)/"
                        + "([0-9]*)/([0-9]*)/([0-9]*)/"
                        + "([0-9A-Za-z-.]*)");
                Matcher verifJoure = patronJoure.matcher(unArticle.adresseXML);
                if (verifJoure.matches()) {
                    uneDate.jour = Integer.parseInt(verifJoure.group(7));
                }
                Pattern patronMois = Pattern.compile("([a-z]*://)"
                        + "([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)/([A-Za-z-0-9]*)/([0-9]*)/"
                        + "([0-9]*)/([0-9]*)/"
                        + "([0-9A-Za-z-.]*)");
                Matcher verifMois = patronMois.matcher(unArticle.adresseXML);
                if (verifMois.matches()) {
                    uneDate.mois = Integer.parseInt(verifMois.group(6));
                }
                Pattern patronAnne = Pattern.compile("([a-z]*://)"
                        + "([A-Za-z-.]*/)"
                        + "([A-Za-z-0-9]*)/([A-Za-z-0-9]*)/([0-9]*)/"
                        + "([0-9]*)/([0-9]*)/"
                        + "([0-9A-Za-z-.]*)");
                Matcher verifAnne = patronAnne.matcher(unArticle.adresseXML);
                if (verifAnne.matches()) {
                    uneDate.annee = Integer.parseInt(verifAnne.group(5));
                }
                //ecrire information sur les articles
                ecrireArticle(fichierBinaire, unArticle, uneDate);
            }
            ++nb;
        } while (nb < nbJourn);

        lecteurFichier.close();//fermer le fichier txt
        fichierBinaire.close();//fermer le fichier binaire
    }

    /**
     * Méthode qui lit l'information d'articles sur fichier XML
     *
     * @param unArticle
     */
    private static void fuchierUrl(Article unArticle) {
        String ligneXml = "";
        try {
// Créer un URL pour la page voulue
            URL url = new URL(unArticle.adresseXML);
// Lire le texte retourné par le serveur
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(url.openStream(),
                    Charset.forName("ISO-8859-1")));

            while ((ligneXml = in.readLine()) != null) {
                Pattern patron5 = Pattern.compile("(.)*(ID=\")"
                        + "([0-9]*)(.)*");
                Matcher verif5 = patron5.matcher(ligneXml);
                if (verif5.matches()) {
                    unArticle.identifiant =
                            Integer.parseInt(verif5.group(3));
                }
                Pattern patron6 = Pattern.compile("(.)*(<Titre>"
                        + ")(.*)(</Titre>)(.)*");
                Matcher verif6 = patron6.matcher(ligneXml);
                if (verif6.matches()) {
                    unArticle.titre = verif6.group(3);
                }
                Pattern patron7 = Pattern.compile("(.)*"
                        + "(<keywordsGoogle>)(.*)"
                        + "(</keywordsGoogle>)(.)*");
                Matcher verif7 = patron7.matcher(ligneXml);
                if (verif7.matches()) {
                    unArticle.motsCles = verif7.group(3);
                }
            }
            in.close();//fermer
        } catch (MalformedURLException e) {
// traitement de l’exception
        } catch (IOException e) {
// traitement de l’exception
        }
    }

    /**
     * Méthode qui ecrit l'information de journalistes sur fichier data
     *
     * @param fichierBinaire
     * @param desJournaliste
     * @param nbArticle
     * @throws IOException
     */
    private static void ecrireJournaliste(DataOutputStream fichierBinaire,
            Journaliste desJournaliste, int nbArticle) throws IOException {

        fichierBinaire.writeInt(desJournaliste.identifiant);
        fichierBinaire.writeUTF(desJournaliste.nom);
        fichierBinaire.writeUTF(desJournaliste.prenom);
        fichierBinaire.writeUTF(desJournaliste.courriel);
        fichierBinaire.writeUTF(desJournaliste.telephone);
        fichierBinaire.writeUTF(desJournaliste.cod);
        fichierBinaire.writeInt(nbArticle);
    }

    /**
     * Méthode qui ecrit l'information d'article sur fichier data
     *
     * @param fichierBinaire
     * @param unArticle
     * @param uneDate
     * @throws IOException
     */
    private static void ecrireArticle(DataOutputStream fichierBinaire,
            Article unArticle, Date uneDate) throws IOException {
        String[] mots = null;//pour compter nombre mots clés
        fichierBinaire.writeInt(unArticle.identifiant);
        fichierBinaire.writeUTF(unArticle.adresseXML);
        fichierBinaire.writeUTF(unArticle.section);
        fichierBinaire.writeUTF(unArticle.categorie);
        fichierBinaire.writeUTF(unArticle.nomFichier);
        fichierBinaire.writeInt(uneDate.jour);
        fichierBinaire.writeInt(uneDate.mois);
        fichierBinaire.writeInt(uneDate.annee);
        fichierBinaire.writeUTF(unArticle.titre);
        mots = unArticle.motsCles.split(",");
        fichierBinaire.writeInt(mots.length);
        fichierBinaire.writeUTF(unArticle.motsCles);
    }
}
