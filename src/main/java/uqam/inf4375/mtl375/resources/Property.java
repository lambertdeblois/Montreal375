package uqam.inf4375.mtl375.resources;

public class Property {
  private float id;
  private float idTrcGeobase;
  private int typeVoie;
  private int typeVoie2;
  private int longueur;
  private int nbrVoie;
  private String separateur;
  private String saisons4;
  private String protege4s;
  private boolean villeMtl;
  private String nomArrVille;

  public Property(float id, float idTrcGeobase, int typeVoie, int typeVoie2, int longueur, int nbrVoie, String separateur, String saisons4, String protege4s, boolean villeMtl, String nomArrVille) {
    this.id = id;
    this.idTrcGeobase = idTrcGeobase;
    this.typeVoie = typeVoie;
    this.typeVoie2 = typeVoie2;
    this.longueur = longueur;
    this.nbrVoie = nbrVoie;
    this.separateur = separateur;
    this.saisons4 = saisons4;
    this.protege4s = protege4s;
    this.villeMtl = villeMtl;
    this.nomArrVille = nomArrVille;
  }

  @JsonProperty public float getId() { return id; }
  @JsonProperty public float getIdTrcGeobase() { return idTrcGeobase; }
  @JsonProperty public int getTypeVoie() { return typeVoie; }
  @JsonProperty public int getTypeVoie2() { return typeVoie2; }
  @JsonProperty public int getLongueur() { return longueur; }
  @JsonProperty public int getNbrVoie() { return nbrVoie; }
  @JsonProperty public String getSeparateur() { return separateur; }
  @JsonProperty public String getSaisons4() { return saisons4; }
  @JsonProperty public String getProtege4s() { return protege4s; }
  @JsonProperty public boolean getVilleMtl() { return villeMtl; }
  @JsonProperty public String getNomArrVille() { return nomArrVille; }

  @Override public String toString() {
    return String.format("%d - %s - %s", id, separateur, saisons4); // TODO: print mieux
  }
}
