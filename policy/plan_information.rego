package terrakube.plan.information

import input as tfplan
  
created := create_count {
    resources := [resource | resource:= tfplan.resource_changes[_]; resource.change.actions[_] == "create"]
    create_count := count(resources)
}

deleted := delete_count {
    resources := [resource | resource:= tfplan.resource_changes[_]; resource.change.actions[_] == "delete"]
    delete_count := count(resources)
}

updated := updated_count {
    resources := [resource | resource:= tfplan.resource_changes[_]; resource.change.actions[_] == "update"]
    updated_count := count(resources)
}

no_change := no_change_count {
    resources := [resource | resource:= tfplan.resource_changes[_]; resource.change.actions[_] == "no-op"]
    no_change_count := count(resources)
}