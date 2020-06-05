package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.SiteCapacityRepository;
import com.amundi.tech.onsite.db.SiteDefinitionRepository;
import com.amundi.tech.onsite.db.model.SiteCapacity;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.exception.AppException;
import com.amundi.tech.onsite.model.Site;
import com.amundi.tech.onsite.model.SiteUsage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/site")
@AllArgsConstructor
@Tag(name = "site", description = "site administration")
public class SiteResource {

    private final SiteDefinitionRepository siteDefinitionRepository;
    private final SiteCapacityRepository siteCapacityRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Site> all(@RequestParam(value = "addStat",required = false) boolean addStat) {
        List<Site> sites = new ArrayList<>();
        List<SiteDefinition> definitions = siteDefinitionRepository.findAll();
        definitions.parallelStream().forEach(d -> {
            List<SiteCapacity> capacities = siteCapacityRepository.findAllBySite(d);
            capacities.forEach(c -> c.setSite(null));
            Site site = Site.builder()
                    .definition(d)
                    .capacities(capacities)
                    .build();
            if (addStat) {
                List<SiteUsage> usages = getSiteUsage(d.getId());
                site.setUsages(usages);
            }
            sites.add(site);
        });
        return sites;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Site byId(@PathVariable("id") long siteId) {
        Optional<SiteDefinition> definitionOptional = siteDefinitionRepository.findById(siteId);
        if (!definitionOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(AppException.SITE_DOESNT_EXIST, siteId));
        }
        SiteDefinition definition = definitionOptional.get();
        Site site = Site.builder()
                .definition(definition)
                .capacities(siteCapacityRepository.findAllBySite(definition))
                .build();

        List<SiteUsage> usages = getSiteUsage(definition.getId());
        site.setUsages(usages);
        return site;
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Site save(@RequestBody Site site) {
        if (site == null || site.getDefinition() == null || site.getDefinition().getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppException.EMPTY_SITE);
        }
        SiteDefinition d = site.getDefinition();

        try {
            sanitizeCapacities(site);
        } catch (AppException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        d = siteDefinitionRepository.save(d);
        siteCapacityRepository.deleteAllBySite(d);

        List<SiteCapacity> updatedCapacities = new ArrayList<>();
        if(site.getCapacities() !=null) {
            for (SiteCapacity capacity : site.getCapacities()) {
                capacity.setSite(d);
                updatedCapacities.add(siteCapacityRepository.save(capacity));
            }
        }
        return Site.builder().definition(d).capacities(updatedCapacities).build();
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") long siteId) {
        siteCapacityRepository.deleteAllBySite(SiteDefinition.builder().id(siteId).build());
        siteDefinitionRepository.deleteById(siteId);
    }

    private List<SiteUsage> getSiteUsage(Long siteId) {
        return new ArrayList<>();
    }

    private void sanitizeCapacities(Site site) throws AppException {
        List<SiteCapacity> capacities = site.getCapacities();

        if(capacities!=null) {
            List<SiteCapacity> updated = new ArrayList<>();
            capacities.sort(Comparator.comparing(SiteCapacity::getStartDate));
            LocalDate date = null;
            for (SiteCapacity capacity : capacities) {
                if (capacity.getStartDate() == null || capacity.getEndDate() == null) {
                    throw new AppException("bad capacity definition");
                }
                if (date != null && !capacity.getStartDate().isAfter(date)) {
                    capacity.setStartDate(date);
                }
                if(!capacity.getStartDate().isAfter(capacity.getEndDate())) {
                    updated.add(capacity);
                    date = capacity.getEndDate().plusDays(1);
                }
            }
            updated.sort(Comparator.comparing(SiteCapacity::getStartDate));
            site.setCapacities(updated);
        }
    }
}
