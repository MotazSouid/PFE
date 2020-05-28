package com.alfresco.api.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.data.AclCapabilities;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PermissionMapping;
import org.apache.chemistry.opencmis.commons.definitions.PermissionDefinition;
import org.apache.chemistry.opencmis.commons.enums.AclPropagation;
import org.apache.chemistry.opencmis.commons.enums.CapabilityAcl;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.json.simple.parser.ParseException;

import com.alfresco.api.example.model.SiteEntry;
import com.alfresco.api.example.model.SiteList;
import com.alfresco.api.example.util.Config;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 *
 * @author motaz souid
 */
public class AclExemple extends LocalConfigExemple {

    public static void main(String[] args) throws IOException, ParseException {
        AclExemple gse = new AclExemple();
        // Méthode de création d'un document dans l'espace du membre connecté
      //gse.creationDocument("Facture");
     // gse.creationDocument("FactureAmani");
        // Ajout des droit d'access au fichier du membre connecté
      gse.applyACL("7578e08c-af67-489f-baa2-adf1bdf4c0c8;1.0", "Ferid", "cmis:read");
   
        }
  
    public void doExample() {
        try {
           
            String homeNetwork = getHomeNetwork();
            GenericUrl sitesUrl = new GenericUrl(getAlfrescoAPIUrl() +
                                                 homeNetwork +
                                                 SITES_URL + "?maxItems=10");
            HttpRequest request = getRequestFactory().buildGetRequest(sitesUrl);
            SiteList siteList = request.execute().parseAs(SiteList.class);
            System.out.println("Up to 10 sites you can see are:");
            for (SiteEntry siteEntry : siteList.list.entries) {
                    System.out.println(siteEntry.entry.id);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Done!");
        
    }
    
    public void getAclCapabilities()
    {
    	LocalConfigExemple prem = new LocalConfigExemple();
    	System.out.println("getting ACL capabilities");
    	AclCapabilities aclCapabilities = prem.getCmisSession().getRepositoryInfo().getAclCapabilities();

    	System.out.println("Propogation for this repository is " + aclCapabilities.getAclPropagation().toString());

    	System.out.println("permissions for this repository are: ");
    	for (PermissionDefinition definition : aclCapabilities.getPermissions()) {
    	    System.out.println(definition.toString());                
    	}

    	System.out.println("\npermission mappings for this repository are: ");
    	Map<String, PermissionMapping> repoMapping = aclCapabilities.getPermissionMapping();
    	for (String key: repoMapping.keySet()) {
    	    System.out.println(key + " maps to " + repoMapping.get(key).getPermissions());                
    	}
    }
    
    public void creationDocument(String nomFichier) {
    	LocalConfigExemple prem = new LocalConfigExemple();
    	Session session = prem.getCmisSession();
    	if (!session.getRepositoryInfo().getCapabilities().getAclCapability()
    	        .equals(CapabilityAcl.MANAGE)) {
    	    System.out.println("Le GED ne supporte pas les ACL");
    	} else {
    	    System.out.println("Le GED supporte les ACL");

    	    System.out.println("Création d'un dossier d'exemple");

    	/*  HashMap<String, String> newFolderProps = new HashMap<String, String>();
    	    newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
    	    newFolderProps.put(PropertyIds.NAME, "Courrier");
    	    Folder folderAssociations = ((Folder) session.getObjectByPath("/Espaces Utilisateurs/motaz")).createFolder(newFolderProps); */

    	    HashMap<String, String> newFileProps = new HashMap<String, String>();
    	    ContentStream contentStream = new ContentStreamImpl("permissions.txt", null,
    	            "plain/text", new ByteArrayInputStream("document facture".getBytes()));

    	    newFileProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
    	    newFileProps.put(PropertyIds.NAME, nomFichier);
    	    Document testDoc = ((Folder) session.getObjectByPath("/Espaces Utilisateurs/"+Config.getConfig().getProperty("username"))).createDocument(newFileProps, contentStream,
    	            VersioningState.MAJOR); 

    	    OperationContext operationContext = new OperationContextImpl();
    	    operationContext.setIncludeAcls(true);
    	    testDoc = (Document) session.getObject(testDoc, operationContext);
    	    System.out.println("l'id du document est: "+testDoc.getId());

    	    System.out.println("ACL avant la création d'un ace...");
    	    Acl acl = testDoc.getAcl();
    	    List<Ace> aces = acl.getAces();
    	    aces.removeAll(aces);
    	    for (Ace ace : aces) {
    	    	 System.out.println("Found ace: " + ace.getPrincipalId() + " toString "+ ace.toString());	 
    	    }
    	    testDoc.setAcl(aces);
    	    testDoc.refresh();
/*
    	    List<String> permissions = new ArrayList<String>();
    	    permissions.add("cmis:all");
    	    String principal = "amani";
    	    Ace aceIn = session.getObjectFactory().createAce(principal, permissions);
    	    List<Ace> aceListIn = new ArrayList<Ace>();
    	    aceListIn.add(aceIn);
    	    testDoc.addAcl(aceListIn, AclPropagation.REPOSITORYDETERMINED);
    	    testDoc = (Document) session.getObject(testDoc, operationContext);
    	   
    	    /*
    	    System.out.println("ACL aprés la création d'un ace...");
    	    for (Ace ace : testDoc.getAcl().getAces()) {
    	      System.out.println("Found ace: " + ace.getPrincipalId() + " toString "+ ace.toString());   
    	    }   
    	    */     
    	    
    	
    	}
    
    }
   
    public void applyACL(String docId, String principlID, String acl) {
    	LocalConfigExemple prem = new LocalConfigExemple();
    	Session session = prem.getCmisSession();
    	Document doc = (Document)session.getObject(docId);
    	List<String> permissions = new ArrayList<String>();
	    permissions.add(acl);
	    String principal = principlID;
	    Ace aceIn = session.getObjectFactory().createAce(principal, permissions);
	    List<Ace> aceListIn = new ArrayList<Ace>();
	    aceListIn.add(aceIn);
	    doc.addAcl(aceListIn, AclPropagation.REPOSITORYDETERMINED);
	 
    }
    
    
}
