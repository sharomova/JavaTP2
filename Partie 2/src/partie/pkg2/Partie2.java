package partie.pkg2;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * TP2 Partie 2
 *
 * Programme d'ajout des information sur les journalistes
 *
 * Date: 9 avril 2014
 *
 * @author Olga Sharomova
 */
public class Partie2 {

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
     * Le nom du fichier txt à écrire
     */
    private static final String NOM_FICHIER = "journalistes.txt";
    /**
     * Le boutone quitter
     */
    private static final int QUITTER = 2;

    public static void main(String[] args) throws IOException {
        Journaliste[] desJournaliste = null;
        // déclaration et initialisation à “rien”
        DataInputStream fichier = null;
        String fichierNom = "";// déclaration nom du fichier
        int noumero = 0;//nombre du journaliste pour ajouter l'information
        int choixMenu;
        int nbJourn = 0;//nombre du journaliste

        fichierNom = ouvrirFichier(fichierNom);
        if (fichierNom != null) {
            desJournaliste = lireFichier(fichierNom, desJournaliste,
                    nbJourn, fichier);

            String textesDesBoutons[] = {
                "Rechercher", // indice rechercher journaliste
                "Rapport", // indice pour ecrir dans un fichier txt
                "Quitter"// indice quitter et reécrir dans un fichier data
            };
            do {
                choixMenu =//indice du bouton qui a été cliqué ou CLOSED_OPTION
                        JOptionPane.showOptionDialog(null,
                        "Choisissez une option",
                        "Gestion des journalistes",
                        0,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // pas d’icone
                        textesDesBoutons, // les textes des boutons
                        textesDesBoutons[2]);   // le bouton par défaut

                switch (choixMenu) {
                    case 0:
                        noumero = rechercherJournaliste(noumero,
                                desJournaliste);
                        if (noumero >= 0) {
                            entrezNom(noumero, desJournaliste);
                            entrezPrenom(noumero, desJournaliste);
                            entrezCourriel(noumero, desJournaliste);
                            entrezTelephone(noumero, desJournaliste);
                            entrezCode(noumero, desJournaliste);
                        }
                        break;
                    case 1:
                        rapport(desJournaliste);

                        break;
                    case 2:
                        quitterProgramme(desJournaliste, fichierNom);
                }
            } while (choixMenu != QUITTER);
        }
    }

    /**
     * Méthode qui lit nom du fichier data
     *
     * @param fichierNom
     * @return nom du fichier
     */
    private static String ouvrirFichier(String fichierNom) {

        fichierNom = (String) JOptionPane.showInputDialog(null, "Entrez le nom",
                "Saisie du nom du fichier", JOptionPane.QUESTION_MESSAGE,
                null, null, "journalistes.data");
        return fichierNom;
    }

    /**
     * Méthode qui crée le tableau des journalistes
     *
     * @param fichierNom
     * @param desJournaliste
     * @param nbJourn
     * @param fichier
     * @return tableau des journalistes
     * @throws IOException
     */
    private static Journaliste[] lireFichier(String fichierNom,
            Journaliste[] desJournaliste, int nbJourn,
            DataInputStream fichier) throws IOException {
        int nb = 0;//nombre pour remplire du tableau
        int nbMots = 0;//nombre mots clés
        int nbArt = 0;//nombre d'articles

        try {
            // Ouverture du fichier
            fichier = new DataInputStream(new FileInputStream(fichierNom));
        } catch (IOException e) {
            System.out.println("Erreur d'ouverture du fichier");
        }
        nbJourn = fichier.readInt();
        //initialiser taile du tableau des journalistes
        desJournaliste = new Journaliste[nbJourn];

        do {
            desJournaliste[nb] = new Journaliste();
            desJournaliste[nb].identifiant = fichier.readInt();
            desJournaliste[nb].nom = fichier.readUTF();
            desJournaliste[nb].prenom = fichier.readUTF();
            desJournaliste[nb].courriel = fichier.readUTF();
            desJournaliste[nb].telephone = fichier.readUTF();
            desJournaliste[nb].cod = fichier.readUTF();
            nbArt = fichier.readInt();
            //initialiser taile du tableau des articles
            desJournaliste[nb].articles = new Article[nbArt];

            for (int i = 0; i < desJournaliste[nb].articles.length; i++) {
                desJournaliste[nb].articles[i] = new Article();
                desJournaliste[nb].articles[i].identifiant = fichier.readInt();
                desJournaliste[nb].articles[i].adresseXML = fichier.readUTF();
                desJournaliste[nb].articles[i].section = fichier.readUTF();
                desJournaliste[nb].articles[i].categorie = fichier.readUTF();
                desJournaliste[nb].articles[i].nomFichier = fichier.readUTF();
                desJournaliste[nb].articles[i].dateCreation = new Date();
                desJournaliste[nb].articles[i].dateCreation.jour =
                        fichier.readInt();
                desJournaliste[nb].articles[i].dateCreation.mois =
                        fichier.readInt();
                desJournaliste[nb].articles[i].dateCreation.annee =
                        fichier.readInt();
                desJournaliste[nb].articles[i].titre = fichier.readUTF();
                nbMots = fichier.readInt();
                desJournaliste[nb].articles[i].motsCles = fichier.readUTF();
            }
            ++nb;
        } while (nb < nbJourn);
        return desJournaliste;
    }

    /**
     * Méthode qui recherche l'identifiant du journaliste
     *
     * @param noumero
     * @param desJournaliste
     * @return l'identifiante du journaliste
     * @throws IOException
     */
    private static int rechercherJournaliste(int noumero,
            Journaliste[] desJournaliste) throws IOException {
        boolean valide = false;//pour relire le message
        int indiceTrouve = -1;//pour trouver 
        String txt = null;
        try {
            do {
                do {
                    txt = JOptionPane.showInputDialog(null,
                            "Entrez le nouméro identifiant du journaliste :",
                            "Saisie de l'indentifiant du journaliste",
                            JOptionPane.PLAIN_MESSAGE);
                    Pattern patronNumero = Pattern.compile("^[0-9]+");
                    Matcher verifNumero = patronNumero.matcher(txt);
                    if (verifNumero.matches()) {
                        noumero = Integer.parseInt(txt);
                        valide = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Numero invalide",
                                "Information", JOptionPane.ERROR_MESSAGE);
                    }
                } while (valide == false);
                for (int i = 0; i < desJournaliste.length; i++) {
                    if (noumero == desJournaliste[i].identifiant) {
                        indiceTrouve = i;
                    }
                }
            } while (indiceTrouve < 0);
        } catch (Exception e) {
        }
        return indiceTrouve;
    }

    /**
     * Méthode qui ajoute le nom du journaliste
     *
     * @param noumero
     * @param desJournaliste
     */
    private static void entrezNom(int noumero, Journaliste[] desJournaliste) {
        boolean valide = false;//pour relire le message
        String nomJourn = null;

        try {
            do {
                nomJourn = JOptionPane.showInputDialog(null,
                        "Entrez le nom du journaliste " + noumero,
                        "Saisie de l'indentifiant sur le journaliste",
                        JOptionPane.PLAIN_MESSAGE);

                Pattern patronNom = Pattern.compile("^[A-Z][a-z -]*[^-]$");
                Matcher verif = patronNom.matcher(nomJourn);
                if (verif.matches()) {
                    desJournaliste[noumero].nom = nomJourn;
                    valide = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Nom invalide",
                            "Information", JOptionPane.ERROR_MESSAGE);
                }
            } while (valide == false);
        } catch (Exception e) {
        }
    }

    /**
     * Méthode qui ajoute le prenom du journaliste
     *
     * @param noumero
     * @param desJournaliste
     */
    private static void entrezPrenom(int noumero,
            Journaliste[] desJournaliste) {
        boolean valide = false;//pour relire le message
        String prenomJourn = null;

        if (desJournaliste[noumero].nom != "") {
            try {
                do {
                    prenomJourn = JOptionPane.showInputDialog(null,
                            "Entrez le prenom du journaliste " + noumero,
                            "Saisie de l'indentifiant sur le journaliste",
                            JOptionPane.PLAIN_MESSAGE);
                    Pattern patronPrenom =
                            Pattern.compile("^[A-Z][a-z -]*[^-]$");
                    Matcher verifPrenom = patronPrenom.matcher(prenomJourn);
                    if (verifPrenom.matches()) {
                        desJournaliste[noumero].prenom = prenomJourn;
                        valide = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Prenom invalide",
                                "Information", JOptionPane.ERROR_MESSAGE);
                    }
                } while (valide == false);

            } catch (Exception e) {
            }
        }
    }

    /**
     * Méthode qui ajoute le courriele du journaliste
     *
     * @param noumero
     * @param desJournaliste
     */
    private static void entrezCourriel(int noumero,
            Journaliste[] desJournaliste) {
        boolean valide = false;//pour relire le message
        String courrieleJourn = null;

        if (desJournaliste[noumero].prenom != "") {
            try {
                do {
                    courrieleJourn = JOptionPane.showInputDialog(null,
                            "Entrez l'adresse de courriel du journaliste "
                            + noumero,
                            "Saisie de l'indentifiant sur le journaliste",
                            JOptionPane.PLAIN_MESSAGE);

                    Pattern patronCourriele = Pattern.compile("^[^.]"
                            + "[0-9a-z_.-]*@[0-9a-z_.-]*[a-z]{2,4}|"
                            + "[a-z]{2}\\.[a-z]{2}$");
                    Matcher verifAdresse =
                            patronCourriele.matcher(courrieleJourn);
                    if (verifAdresse.matches()) {
                        desJournaliste[noumero].courriel = courrieleJourn;
                        valide = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Adresse de courriele invalide",
                                "Information", JOptionPane.ERROR_MESSAGE);
                    }
                } while (valide == false);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Méthode qui ajoute le numero de telephone du journaliste
     *
     * @param noumero
     * @param desJournaliste
     */
    private static void entrezTelephone(int noumero,
            Journaliste[] desJournaliste) {
        boolean valide = false;//pour relire le message
        String telephoneJourn = null;

        if (desJournaliste[noumero].telephone != "") {
            try {
                do {
                    telephoneJourn = JOptionPane.showInputDialog(null,
                            "Entrez le numero de telepone du journaliste "
                            + noumero,
                            "Saisie de l'indentifiant sur le journaliste",
                            JOptionPane.PLAIN_MESSAGE);
                    Pattern patronTelephone = Pattern.compile("^\\([2-9]"
                            + "[0-9]{2}\\) [2-9][0-9]{2}\\-[0-9]{4}");
                    Matcher verifTel = patronTelephone.matcher(telephoneJourn);
                    if (verifTel.matches()) {
                        desJournaliste[noumero].telephone = telephoneJourn;
                        valide = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Telephone invalide",
                                "Information", JOptionPane.ERROR_MESSAGE);
                    }
                } while (valide == false);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Méthode qui ajoute le code d'utilisateur du journaliste
     *
     * @param noumero
     * @param desJournaliste
     */
    private static void entrezCode(int noumero, Journaliste[] desJournaliste) {
        boolean valide = false;//pour relire le message
        String codeJourn = null;
        if (desJournaliste[noumero].cod != "") {
            try {
                do {
                    codeJourn = JOptionPane.showInputDialog(null,
                            "Entrez le code d'utilisateure du journaliste "
                            + noumero,
                            "Saisie de l'indentifiant sur le journaliste",
                            JOptionPane.PLAIN_MESSAGE);
                    Pattern patronCode =
                            Pattern.compile("^[A-Z][a-z0-9@!?-_]{5,11}");
                    Matcher verifCode = patronCode.matcher(codeJourn);
                    if (verifCode.matches()) {
                        desJournaliste[noumero].cod = codeJourn;
                        valide = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Code d'utilisateure invalide",
                                "Information", JOptionPane.ERROR_MESSAGE);
                    }
                } while (valide == false);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Méthode qui ecrit dans un fichier txt
     *
     * @param desJournaliste
     */
    private static void rapport(Journaliste[] desJournaliste) {
        // déclaration et initialisation à “rien”
        PrintWriter createurFichier = null;
        int nb = 0;//nombre pour un tableau

        try {
            // Ouverture du fichier
            createurFichier =
                    new PrintWriter(new BufferedOutputStream
                    (new FileOutputStream(NOM_FICHIER)));
        } catch (IOException e) {
            System.out.println("Erreur d'ouverture du fichier,"
                    + " arrêt du programme");
        }
        createurFichier.println("INFORMATION SUR LES JOURNALISTES\n");
        createurFichier.println("=================================\n");
        do {
            createurFichier.println("-----------Journaliste-----------\n");
            createurFichier.println("ID : " + desJournaliste[nb].identifiant);
            createurFichier.println("Nom : " + desJournaliste[nb].nom);
            createurFichier.println("Prénom : " + desJournaliste[nb].prenom);
            createurFichier.println("Courriel : "
                    + desJournaliste[nb].courriel);
            createurFichier.println("Téléphone : "
                    + desJournaliste[nb].telephone);
            createurFichier.println("Code d'utilisateur : "
                    + desJournaliste[nb].cod);
            for (int i = 0; i < desJournaliste[nb].articles.length; i++) {
                createurFichier.println("-----------Article-----------\n");
                createurFichier.println("ID : "
                        + desJournaliste[nb].articles[i].identifiant);
                createurFichier.println("Adresse : "
                        + desJournaliste[nb].articles[i].adresseXML);
                createurFichier.println("Section : "
                        + desJournaliste[nb].articles[i].section);
                createurFichier.println("Categorie : "
                        + desJournaliste[nb].articles[i].categorie);
                createurFichier.println("Fichier : "
                        + desJournaliste[nb].articles[i].nomFichier);
                createurFichier.println("Date de creation : "
                        + desJournaliste[nb].articles[i].dateCreation.jour + "/"
                        + desJournaliste[nb].articles[i].dateCreation.mois + "/"
                        + desJournaliste[nb].articles[i].dateCreation.annee);
                createurFichier.println("Titre : "
                        + desJournaliste[nb].articles[i].titre);
                createurFichier.println("Mots clés : "
                        + desJournaliste[nb].articles[i].motsCles);
            }
            ++nb;
        } while (nb < desJournaliste.length);
        createurFichier.close();    //fermer le fichier txt
    }

    /**
     * Méthode qui reécrit dans un fichier data
     *
     * @param desJournaliste
     * @param fichierNom
     * @throws IOException
     */
    private static void quitterProgramme(Journaliste[] desJournaliste,
            String fichierNom) throws IOException {
        // déclaration et initialisation à “rien”
        DataOutputStream fichierBinaire = null;
        String[] mots = null;
        int nb = 0;//nombre pour un tableau

        try {
            // Ouverture du fichier
            fichierBinaire = new DataOutputStream(
                    new FileOutputStream(fichierNom));
        } catch (IOException e) {
            System.out.println("Erreur d'ouverture du fichier,"
                    + " arrêt du programme");
        }
        fichierBinaire.writeInt(desJournaliste.length);
        do {
            fichierBinaire.writeInt(desJournaliste[nb].identifiant);
            fichierBinaire.writeUTF(desJournaliste[nb].nom);
            fichierBinaire.writeUTF(desJournaliste[nb].prenom);
            fichierBinaire.writeUTF(desJournaliste[nb].courriel);
            fichierBinaire.writeUTF(desJournaliste[nb].telephone);
            fichierBinaire.writeUTF(desJournaliste[nb].cod);
            fichierBinaire.writeInt(desJournaliste[nb].articles.length);
            for (int i = 0; i < desJournaliste[nb].articles.length; i++) {
                fichierBinaire.writeInt(desJournaliste[nb].articles[i].identifiant);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].adresseXML);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].section);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].categorie);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].nomFichier);
                fichierBinaire.writeInt(desJournaliste[nb].articles[i].dateCreation.jour);
                fichierBinaire.writeInt(desJournaliste[nb].articles[i].dateCreation.mois);
                fichierBinaire.writeInt(desJournaliste[nb].articles[i].dateCreation.annee);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].titre);
                mots = desJournaliste[nb].articles[i].motsCles.split(",");
                fichierBinaire.writeInt(mots.length);
                fichierBinaire.writeUTF(desJournaliste[nb].articles[i].motsCles);
            }
            ++nb;
        } while (nb < desJournaliste.length);
        fichierBinaire.close();  //fermer le fichier data
    }
}
