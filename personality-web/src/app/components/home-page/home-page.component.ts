import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {PersonalityTestDialogComponent} from "../personality-test-dialog/personality-test-dialog.component";

@Component({
	selector: 'app-home-page',
	templateUrl: './home-page.component.html',
	styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit {

	constructor(private dialog: MatDialog) {
	}

	ngOnInit(): void {
	}

	openPersonalityTestDialog() {
		this.dialog.open(PersonalityTestDialogComponent, {
			width: '60%'
		});
	}

}
