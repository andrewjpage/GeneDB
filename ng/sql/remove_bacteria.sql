update 	organismprop set (value) = ('no' )
FROM 
    organism, 
    cvterm
WHERE
    organism.common_name in (
      'Bfragilis_NCTC9343',
      'Cjejuni',
      'Cabortus',
      'Cdiphtheriae',
      'Ecarotovora',
      'Rleguminosarum',
      'Bbronchiseptica',
      'Bparapertussis',
      'Bpertussis',
      'Bcenocepacia',
      'Bpseudomallei',
      'Saureus_MRSA252',
      'Saureus_MSSA476',
      'Spyogenes',
      'Suberis',
      'Saureus_EMRSA15',
      'Spneumoniae_ATCC700669',
      'Spneumoniae_D39',
      'Spneumoniae_OXC141',
      'Spneumoniae_TIGR4',
      'Saureus_TW20',
      'Scoelicolor',
      'Styphi',
      'Saureus_LGA251',
      'Styphimurium',
      'Saureus_ST398'
      )
AND organismprop.organism_id = organism.organism_id
AND organismprop.type_id = cvterm.cvterm_id 
AND cvterm.name = 'genedb_public';
		
		
update 	organismprop set (value) = ('no' )
FROM 
    organism, 
    cvterm
WHERE
    organism.common_name in (
	'Bfragilis_NCTC9343',
	'Cjejuni',
	'Cabortus',
	'Cdiphtheriae',
	'Ecarotovora',
	'Rleguminosarum',
	'Bbronchiseptica',
	'Bparapertussis',
	'Bpertussis',
	'Bcenocepacia',
	'Bpseudomallei',
	'Saureus_MRSA252',
	'Saureus_MSSA476',
	'Spyogenes',
	'Suberis',
	'Saureus_EMRSA15',
	'Spneumoniae_ATCC700669',
	'Spneumoniae_D39',
	'Spneumoniae_OXC141',
	'Spneumoniae_TIGR4',
	'Saureus_TW20',
	'Scoelicolor',
	'Styphi',
	'Saureus_LGA251',
	'Styphimurium',
	'Saureus_ST398'
	)
AND organismprop.organism_id = organism.organism_id
AND organismprop.type_id = cvterm.cvterm_id 
AND cvterm.name = 'webservices_public';
