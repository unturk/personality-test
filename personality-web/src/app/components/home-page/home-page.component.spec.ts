import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HomePageComponent} from './home-page.component';
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {AngularMaterialModule} from "../../modules/angular-material.module";

describe('HomePageComponent', () => {
	let component: HomePageComponent;
	let fixture: ComponentFixture<HomePageComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [HomePageComponent],
			schemas: [NO_ERRORS_SCHEMA],
			imports: [NoopAnimationsModule, AngularMaterialModule],
		})
			.compileComponents();
	});

	beforeEach(() => {
		fixture = TestBed.createComponent(HomePageComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
