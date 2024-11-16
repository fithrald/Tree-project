package TreeProject.controllers;

import TreeProject.models.Person;
import TreeProject.models.Tree;
import TreeProject.models.TreeDTO;
import TreeProject.repositories.TreeRepository;
import TreeProject.security.PersonDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/tree")
public class TreeController {
    @Autowired
    private TreeRepository treeRepository;

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }
    @GetMapping("/getMap")
    public String getMap(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        List<Tree> trees = treeRepository.findByPersonId(personDetails.getPerson().getId());
        List<TreeDTO> treeDTOs = trees.stream()
                .map(tree -> TreeDTO.builder().lat(Double.parseDouble(tree.getLat()))
                        .lng( Double.parseDouble(tree.getLng()))
                        .treeType(tree.getTreeType())
                        .treeName(tree.getTreeName()).build())
                .collect(Collectors.toList());


        model.addAttribute("userTrees", treeDTOs);
        return "treeMap";
    }
    @GetMapping("/getWatchMap")
    public String getWatchMap(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        List<Tree> trees = treeRepository.findByPersonId(personDetails.getPerson().getId());
        List<TreeDTO> treeDTOs = trees.stream()
                .map(tree -> TreeDTO.builder().lat(Double.parseDouble(tree.getLat()))
                        .lng( Double.parseDouble(tree.getLng()))
                        .treeType(tree.getTreeType())
                        .treeName(tree.getTreeName()).build())
                .collect(Collectors.toList());


        model.addAttribute("userTrees", treeDTOs);
        return "watchtreeMap";
    }
    @PostMapping("/payment")
    public String getPayment(@RequestParam("markersData") String markersData, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TreeDTO> treeList = Collections.emptyList();

        try {
            treeList = objectMapper.readValue(markersData, new TypeReference<List<TreeDTO>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        double totalCost = treeList.stream()
                .mapToDouble(TreeDTO::getPrice)
                .sum();
        double discount = totalCost * 0.05;
        model.addAttribute("markersData", markersData);
        model.addAttribute("treeList", treeList);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("discount", discount);
        return "payment";
    }
    @PostMapping("/save")
    public String payAndSaveMarkers(@AuthenticationPrincipal PersonDetails personDetails,
                                    @RequestParam("totalCost") String totalCost,
                                    @RequestParam("markersData") String markersData,
                                    Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TreeDTO> treeDTOS = new ArrayList<>();
        try {
            treeDTOS = objectMapper.readValue(markersData, new TypeReference<List<TreeDTO>>() {});

            List<Tree> trees = treeDTOS.stream()
                    .map(treeDTO -> convertToEntity(treeDTO, personDetails.getPerson()))
                    .collect(Collectors.toList());
            treeRepository.saveAll(trees);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        double totalCostValue = Double.parseDouble(totalCost);
        double discount = totalCostValue * 0.05;
        String certificateNumber = UUID.randomUUID().toString();
        String promoCode = "AMZN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        model.addAttribute("userName", personDetails.getPerson().getUsername());
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("trees", treeDTOS);
        model.addAttribute("certificateNumber", certificateNumber);
        model.addAttribute("discount", discount);
        model.addAttribute("promoCode", promoCode);
        return "success";
    }

    private Tree convertToEntity(TreeDTO treeDTO, Person person) {
        Tree tree = new Tree();
        tree.setLat(String.valueOf(treeDTO.getLat()));
        tree.setLng(String.valueOf(treeDTO.getLng()));
        tree.setTreeType(treeDTO.getTreeType());
        tree.setTreeName(treeDTO.getTreeName());
        tree.setPerson(person);
        return tree;
    }
}
