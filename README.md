# medlemskap-barnebriller
Medlemskap-barnebriller er en fasade komponent for Barnebriller for medlemskaps vurdering.



## URL til tjeneste
* preprod: https://medlemskap-barnebriller.dev.intern.nav.no/barnebriller
* prod: https://medlemskap-barnebriller.intern.nav.no/barnebriller

## Autentisering
Forventer et AzureAD-token utstedt til servicebruker, satt Authorization-header (Bearer)

## Azure AD Scope
| Azure scope                                                | Miljø    |
|------------------------------------------------------------|----------|
| api://dev-gcp.medlemskap.medlemskap-barnebriller/.default  | GCP-DEV  |
| api://prod-gcp.medlemskap.medlemskap-barnebriller/.default | GCP-PROD |

## Headere
I tillegg til Authorization-headeren kreves det at Content-Type er satt til application/json


# Test brukere i Dolly
https://confluence.adeo.no/display/TLM/test+data

## Eksempel på kall

Kallet er en POST mot url definert over

```
{
"fnr":"12345678912",
"bestillingsDato" :"2007-02-02"
}
```

### Inn-parametre
* fnr: Fødselsnummer, identifiserer brukeren
* bestillingsDato, Dato for når bestillingen utføres

Eksempel på respons 
```
{
  "medlemskapBevist" : true,
  "uavklartMedlemskap" : false,
  "saksgrunnlag" : [ {
    "kilde" : "MEDLEMSKAP_BARN",
    "saksgrunnlag" : {
      "fnr" : "25500995664",
      "bestillingsdato" : "2023-03-24",
      "correlation-id" : "9229daab-7db3-4769-9581-32500fe48ff5"
    }
  }, {
    "kilde" : "PDL",
    "saksgrunnlag" : {
      "fnr" : "25500995664",
      "pdl" : {
        "hentPerson" : {
          "bostedsadresse" : [ {
            "gyldigFraOgMed" : "2020-05-01T00:00",
            "gyldigTilOgMed" : null,
            "vegadresse" : {
              "matrikkelId" : "154918011",
              "bruksenhetsnummer" : null
            },
            "matrikkeladresse" : null,
            "ukjentBosted" : null
          }, {
            "gyldigFraOgMed" : "2020-05-01T00:00",
            "gyldigTilOgMed" : null,
            "vegadresse" : {
              "matrikkelId" : "154918011",
              "bruksenhetsnummer" : null
            },
            "matrikkeladresse" : null,
            "ukjentBosted" : null
          } ],
          "deltBosted" : [ {
            "startdatoForKontrakt" : "2020-11-01",
            "sluttdatoForKontrakt" : null,
            "vegadresse" : {
              "matrikkelId" : "154924181",
              "bruksenhetsnummer" : null
            },
            "matrikkeladresse" : null,
            "ukjentBosted" : null
          }, {
            "startdatoForKontrakt" : "2020-11-01",
            "sluttdatoForKontrakt" : null,
            "vegadresse" : {
              "matrikkelId" : "154924181",
              "bruksenhetsnummer" : null
            },
            "matrikkeladresse" : null,
            "ukjentBosted" : null
          } ],
          "adressebeskyttelse" : [ ],
          "foedsel" : [ {
            "foedselsaar" : 2009,
            "foedselsdato" : "2009-10-25"
          }, {
            "foedselsaar" : 2009,
            "foedselsdato" : "2009-10-25"
          } ],
          "foreldreansvar" : [ {
            "ansvar" : "mor",
            "ansvarlig" : "12467300103",
            "folkeregistermetadata" : {
              "gyldighetstidspunkt" : "2023-03-24T16:04:11",
              "opphoerstidspunkt" : null
            }
          }, {
            "ansvar" : "mor",
            "ansvarlig" : "12467300103",
            "folkeregistermetadata" : {
              "gyldighetstidspunkt" : "2023-03-24T16:04:11",
              "opphoerstidspunkt" : null
            }
          } ],
          "forelderBarnRelasjon" : [ {
            "relatertPersonsIdent" : "12467300103",
            "relatertPersonsRolle" : "MOR",
            "minRolleForPerson" : "BARN",
            "folkeregistermetadata" : {
              "gyldighetstidspunkt" : "2023-03-24T16:04:11",
              "opphoerstidspunkt" : null
            }
          }, {
            "relatertPersonsIdent" : "12467300103",
            "relatertPersonsRolle" : "MOR",
            "minRolleForPerson" : "BARN",
            "folkeregistermetadata" : {
              "gyldighetstidspunkt" : "2023-03-24T16:04:11",
              "opphoerstidspunkt" : null
            }
          } ],
          "fullmakt" : [ ],
          "vergemaalEllerFremtidsfullmakt" : [ ]
        }
      }
    }
  }, {
    "kilde" : "PDL",
    "saksgrunnlag" : {
      "rolle" : "FORELDER_ANSVAR-mor",
      "fnr" : "12467300103",
      "pdl" : {
        "hentPerson" : {
          "bostedsadresse" : [ {
            "gyldigFraOgMed" : "2020-05-01T01:00",
            "gyldigTilOgMed" : null,
            "vegadresse" : {
              "matrikkelId" : "154918011",
              "bruksenhetsnummer" : null
            },
            "matrikkeladresse" : null
          } ],
          "adressebeskyttelse" : [ ]
        }
      }
    }
  }, {
    "kilde" : "LOV_ME",
    "saksgrunnlag" : {
      "rolle" : "FORELDER_ANSVAR-mor",
      "fnr" : "12467300103",
      "lov_me" : {
        "resultat" : {
          "regelId" : "REGEL_MEDLEM_KONKLUSJON",
          "svar" : "JA",
          "årsaker" : [ ]
        }
      },
      "correlation-id-subcall-medlemskap" : "9229daab-7db3-4769-9581-32500fe48ff5+f6195f3b-016a-41bb-bada-8f4f1c312d8b"
    }
  } ]
}
```
