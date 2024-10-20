package TreeProject.controllers;

import TreeProject.models.Tree;
import TreeProject.models.TreeDTO;
import TreeProject.repositories.TreeRepository;
import TreeProject.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/tree")
public class TreeController {
    @Autowired
    private TreeRepository treeRepository;

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
}
