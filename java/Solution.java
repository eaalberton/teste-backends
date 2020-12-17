import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entities.Proponent;
import entities.Proposal;
import entities.Warranty;
import enums.EnumAction;

public class Solution {
	// Essa função recebe uma lista de mensagens, por exemplo:
	//
	// [
	// "72ff1d14-756a-4549-9185-e60e326baf1b,proposal,created,2019-11-11T14:28:01Z,80921e5f-4307-4623-9ddb-5bf826a31dd7,1141424.0,240",
	// "af745f6d-d5c0-41e9-b04f-ee524befa425,warranty,added,2019-11-11T14:28:01Z,80921e5f-4307-4623-9ddb-5bf826a31dd7,31c1dd83-8fb7-44ff-8cb7-947e604f6293,3245356.0,DF",
	// "450951ee-a38d-475c-ac21-f22b4566fb29,warranty,added,2019-11-11T14:28:01Z,80921e5f-4307-4623-9ddb-5bf826a31dd7,c8753500-1982-4003-8287-3b46c75d4803,3413113.45,DF",
	// "66882b68-baa4-47b1-9cc7-7db9c2d8f823,proponent,added,2019-11-11T14:28:01Z,80921e5f-4307-4623-9ddb-5bf826a31dd7,3f52890a-7e9a-4447-a19b-bb5008a09672,Ismael
	// Streich Jr.,42,62615.64,true"
	// ]
	//
	// Complete a função para retornar uma String com os IDs das propostas válidas
	// no seguinte formato (separado por vírgula):
	// "52f0b3f2-f838-4ce2-96ee-9876dd2c0cf6,51a41350-d105-4423-a9cf-5a24ac46ae84,50cedd7f-44fd-4651-a4ec-f55c742e3477"

	private static final String DELIMITER = ",";
	private static final String PROPOSAL = "proposal";
	private static final String WARRANTY = "warranty";
	private static final String PROPONENT = "proponent";

	private static final int EVENT_SCHEMA_INDEX = 1;

	public static String processMessages(List<String> messages) {

		Map<String, Proposal> mapId_proposal = new LinkedHashMap<String, Proposal>();

		generateEntities(messages, mapId_proposal);

		return getIdsValidProposal(mapId_proposal);
	}

	private static String getIdsValidProposal(Map<String, Proposal> mapId_proposal) {
		
		StringBuilder sbIdsProposal = new StringBuilder();
		
		for (String idProposal : mapId_proposal.keySet()) {
			
			Proposal proposal = mapId_proposal.get(idProposal);
			
			validateProposalProponent(proposal);
			
			validateProposalWarranty(proposal);
			
			if(proposal.isValid()) {
				if(!sbIdsProposal.toString().isEmpty()) {
					sbIdsProposal.append(",");
				}
				sbIdsProposal.append(idProposal);
			}
		}
		
		return sbIdsProposal.toString();
	}

	private static void validateProposalWarranty(Proposal proposal) {
		
		if(proposal.getMapId_warranty().keySet().size() < 1) {
			proposal.setValid(false);
			return;
		}
		
		BigDecimal doubleProposalValue = proposal.getLoanValue().multiply(new BigDecimal(2));
		
		BigDecimal amountWarrantyValue = proposal.getMapId_warranty().values().stream()
			    .map(Warranty::getValue)
			    .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		if(amountWarrantyValue.compareTo(doubleProposalValue) < 0) {
			proposal.setValid(false);
		}
		
	}

	private static void validateProposalProponent(Proposal proposal) {
		
		if(proposal.getMapId_proponent().keySet().size() < 2) {
			proposal.setValid(false);
			return;
		}
		
		int numberOfMainProponent = 0;
		for (Proponent proponent : proposal.getMapId_proponent().values()) {
			if(proponent.isMain()) {
				numberOfMainProponent++;
			}
		}
		
		if(numberOfMainProponent != 1) {
			proposal.setValid(false);
		}
		
	}

	private static void validateProposal(Map<String, Proposal> mapId_proposal, Proposal proposal) {
		
		validateDelayedProposalEvent(proposal, mapId_proposal);
		
		switch (proposal.getEventAction()) {
		case CREATED:
		case UPDATED:
			validateProposalLoanValue(proposal);

			validateNumberMonthlyInstallments(proposal);

			if(proposal.isValid()) {
				mapId_proposal.put(proposal.getId(), proposal);
			}
			break;
		case DELETED:
			mapId_proposal.remove(proposal.getId());
			break;

		default:
			break;
		}
	}

	private static void validateWarranty(Map<String, Proposal> mapId_proposal, Warranty warranty) {
		
		if(!mapId_proposal.containsKey(warranty.getProposalId())) {
			return;
		}
		
		validateDelayedWarrantyEvent(warranty, mapId_proposal);
		
		switch (warranty.getEventAction()) {
		case ADDED:
		case UPDATED:
			validateWarrantyProvince(warranty);
			
			if(warranty.isValid()) {
				mapId_proposal.get(warranty.getProposalId()).getMapId_warranty().put(warranty.getId(), warranty);
			} else {
				mapId_proposal.remove(warranty.getProposalId());
			}
			break;
		case REMOVED:
			mapId_proposal.get(warranty.getProposalId()).getMapId_warranty().remove(warranty.getId());
			break;
			
		default:
			break;
		}
	}

	private static void validateProponent(Map<String, Proposal> mapId_proposal, Proponent proponent) {
		
		if(!mapId_proposal.containsKey(proponent.getProposalId())) {
			return;
		}
		
		validateDelayedProponentEvent(proponent, mapId_proposal);
		
		switch (proponent.getEventAction()) {
		case ADDED:
		case UPDATED:
			validateProponentAge(proponent);
			
			validateProponentIncome(proponent, mapId_proposal);
			
			if(proponent.isValid()) {
				mapId_proposal.get(proponent.getProposalId()).getMapId_proponent().put(proponent.getId(), proponent);
			}
			break;
		case REMOVED:
			mapId_proposal.get(proponent.getProposalId()).getMapId_warranty().remove(proponent.getId());
			break;
			
		default:
			break;
		}
	}

	private static void validateProponentIncome(Proponent proponent, Map<String, Proposal> mapId_proposal) {
		
		Proposal proposal = mapId_proposal.get(proponent.getProposalId());
		
		if(proposal != null && proponent.isMain()) {
			
			BigDecimal desiredIncome = new BigDecimal(0);
			
			BigDecimal installmentValue = proposal.getLoanValue()
					.divide(new BigDecimal(proposal.getNumberOfMonthLyInstallments()), 2, RoundingMode.HALF_UP);
			
			if(proponent.getAge().compareTo(Integer.valueOf(18)) >= 0 && proponent.getAge().compareTo(Integer.valueOf(24)) <= 0) {
				desiredIncome = installmentValue.multiply(new BigDecimal(4));
				
			} else if(proponent.getAge().compareTo(Integer.valueOf(24)) >= 0 && proponent.getAge().compareTo(Integer.valueOf(50)) <= 0) {
				desiredIncome = installmentValue.multiply(new BigDecimal(3));
			
			} else if(proponent.getAge().compareTo(Integer.valueOf(50)) > 0) {
				desiredIncome = installmentValue.multiply(new BigDecimal(2));
			}
			
			if(proponent.getMonthlyIncome().compareTo(desiredIncome) < 0) {
				proponent.setValid(false);
			}
		}
	}

	private static void validateProponentAge(Proponent proponent) {
		
		if(proponent.getAge().compareTo(Integer.valueOf(18)) < 0) {
			proponent.setValid(false);
		}
		
	}

	private static void validateWarrantyProvince(Warranty warranty) {
		
		if(Arrays.asList("PR", "SC", "RS").contains(warranty.getProvince())) {
			warranty.setValid(false);
		}
		
	}

	private static void validateDelayedProposalEvent(Proposal proposal, Map<String, Proposal> mapId_proposal) {
		
		for (Proposal processedProposal : mapId_proposal.values()) {
			
			if(proposal.getEventAction().equals(processedProposal.getEventAction())
					&& proposal.getId().equals(processedProposal.getId())
					&& proposal.getEventTimeStamp().isBefore(processedProposal.getEventTimeStamp())) {
				
				proposal.setValid(false);
			}
		}
	}
	
	private static void validateDelayedWarrantyEvent(Warranty warranty, Map<String, Proposal> mapId_proposal) {
		
		Proposal proposal = mapId_proposal.get(warranty.getProposalId()); 
		
		for (Warranty warrantyProposal : proposal.getMapId_warranty().values()) {
			
			if (warranty.getEventAction().equals(warrantyProposal.getEventAction())
					&& warranty.getId().equals(warrantyProposal.getId())
					&& warranty.getEventTimeStamp().isBefore(warrantyProposal.getEventTimeStamp())) {
				
				warranty.setValid(false);
			}
		}
	}

	private static void validateDelayedProponentEvent(Proponent proponent, Map<String, Proposal> mapId_proposal) {
		
		Proposal proposal = mapId_proposal.get(proponent.getProposalId()); 
		
		for (Proponent proponentProposal : proposal.getMapId_proponent().values()) {
			
			if (proponent.getEventAction().equals(proponentProposal.getEventAction())
					&& proponent.getId().equals(proponentProposal.getId())
					&& proponent.getEventTimeStamp().isBefore(proponentProposal.getEventTimeStamp())) {
				
				proponent.setValid(false);
			}
		}
	}

	private static void validateNumberMonthlyInstallments(Proposal proposal) {
		
		if(!(proposal.getNumberOfMonthLyInstallments().compareTo(Integer.valueOf(24)) >= 0 
				&& proposal.getNumberOfMonthLyInstallments().compareTo(Integer.valueOf(180)) <= 0)) {
			
			proposal.setValid(false); 
		}
	}

	private static void validateProposalLoanValue(Proposal proposal) {
		
		if(!(proposal.getLoanValue().compareTo(new BigDecimal("30000.00")) >= 0 
				&& proposal.getLoanValue().compareTo(new BigDecimal("3000000.00")) <= 0)) {
			
			proposal.setValid(false);
		}
	}

	private static void generateEntities(List<String> messages, Map<String, Proposal> mapId_proposal) {
		
		List<String> listIdEvent = new ArrayList<String>();
		
		for (String message : messages) {

			String[] splitMessage = message.split(DELIMITER);
			
			if(!isValidEventId(splitMessage[0], listIdEvent)) {
				continue;
			}

			switch (splitMessage[EVENT_SCHEMA_INDEX]) {
			case PROPOSAL:
				validateProposal(mapId_proposal, generateProposal(splitMessage));
				break;
			case WARRANTY:
				validateWarranty(mapId_proposal, generateWarranty(splitMessage));
				break;
			case PROPONENT:
				validateProponent(mapId_proposal, generateProponent(splitMessage));
				break;

			default:
				break;
			}
		}
	}

	private static boolean isValidEventId(String eventId, List<String> listIdEvent) {
		
		if (listIdEvent.contains(eventId)) {
			return false;
		}
		
		listIdEvent.add(eventId);
		
		return true;
	}

	private static Proposal generateProposal(String[] splitMessage) {
		Proposal proposal = new Proposal();

		proposal.setEventId(splitMessage[0]);
		proposal.setEventSchema(splitMessage[1]);
		proposal.setEventAction(getActionEnum(splitMessage[2]));
		proposal.setEventTimeStamp(ZonedDateTime.parse(splitMessage[3]).toLocalDateTime());
		proposal.setId(splitMessage[4]);

		if (!EnumAction.DELETED.equals(proposal.getEventAction())) {
			proposal.setLoanValue(new BigDecimal(splitMessage[5]));
			proposal.setNumberOfMonthLyInstallments(Integer.parseInt(splitMessage[6]));
		}
		return proposal;
	}

	private static EnumAction getActionEnum(String action) {
		switch (action) {
		case "created":
			return EnumAction.CREATED;
		case "updated":
			return EnumAction.UPDATED;
		case "deleted":
			return EnumAction.DELETED;
		case "added":
			return EnumAction.ADDED;
		case "removed":
			return EnumAction.REMOVED;

		default:
			return null;
		}
	}

	private static Warranty generateWarranty(String[] splitMessage) {
		Warranty warranty = new Warranty();

		warranty.setEventId(splitMessage[0]);
		warranty.setEventSchema(splitMessage[1]);
		warranty.setEventAction(getActionEnum(splitMessage[2]));
		warranty.setEventTimeStamp(ZonedDateTime.parse(splitMessage[3]).toLocalDateTime());
		warranty.setProposalId(splitMessage[4]);
		warranty.setId(splitMessage[5]);

		if (!EnumAction.REMOVED.equals(warranty.getEventAction())) {
			warranty.setValue(new BigDecimal(splitMessage[6]));
			warranty.setProvince(splitMessage[7]);
		}
		return warranty;
	}

	private static Proponent generateProponent(String[] splitMessage) {
		Proponent proponent = new Proponent();

		proponent.setEventId(splitMessage[0]);
		proponent.setEventSchema(splitMessage[1]);
		proponent.setEventAction(getActionEnum(splitMessage[2]));
		proponent.setEventTimeStamp(ZonedDateTime.parse(splitMessage[3]).toLocalDateTime());
		proponent.setProposalId(splitMessage[4]);
		proponent.setId(splitMessage[5]);

		if (!EnumAction.REMOVED.equals(proponent.getEventAction())) {
			proponent.setName(splitMessage[6]);
			proponent.setAge(Integer.parseInt(splitMessage[7]));
			proponent.setMonthlyIncome(new BigDecimal(splitMessage[8]));
			proponent.setMain(splitMessage[9].equals("true"));
		}
		return proponent;
	}
}
